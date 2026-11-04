package vn.taphoagan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import vn.taphoagan.domain.*;
import vn.taphoagan.repository.ProductRepository;
import vn.taphoagan.repository.StoreRepository;
import vn.taphoagan.repository.UserRepository;
import vn.taphoagan.service.*;
import vn.taphoagan.web.form.CheckoutForm;
import vn.taphoagan.web.form.StoreForm;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class ApplicationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine");

    @Autowired UserService users;
    @Autowired UserRepository userRepository;
    @Autowired StoreService storeService;
    @Autowired StoreRepository storeRepository;
    @Autowired CatalogService catalog;
    @Autowired ProductRepository products;
    @Autowired OrderService orders;
    @Autowired MockMvc mvc;

    private User owner;
    private User customer;
    private Store store;
    private Product product;

    @BeforeEach
    void setUp() {
        owner = users.provision("owner-test@example.com", "Chủ tiệm test", null);
        customer = users.provision("customer-test@example.com", "Khách test", null);
        if (storeRepository.findByOwnerId(owner.getId()).isEmpty()) {
            StoreForm form = new StoreForm();
            form.setName("Tiệm test");
            form.setAddress("1 Test, Phường Bến Thành");
            form.setLat(10.7725);
            form.setLng(106.6980);
            form.setPhone("0901234567");
            form.setRadiusKm(BigDecimal.valueOf(2));
            form.setDeliveryEnabled(true);
            form.setPickupEnabled(true);
            form.setDeliveryFee(10000);
            form.setMinOrder(0);
            form.setOpenMinute(0);
            form.setCloseMinute(1439);
            store = storeService.saveForOwner(owner, form);
        } else {
            store = storeRepository.findByOwnerId(owner.getId()).orElseThrow();
        }
        product = catalog.createOrUpdate(store.getId(), productForm());
    }

    private vn.taphoagan.web.form.ProductForm productForm() {
        var form = new vn.taphoagan.web.form.ProductForm();
        form.setBarcode("893999900001");
        form.setName("Món test");
        form.setPrice(35000);
        form.setUnit("chai");
        form.setStock(10);
        form.setActive(true);
        return form;
    }

    @Test
    void orderLifecycleReservesAndCompletes() {
        Cart cart = new Cart();
        cart.setStoreId(store.getId());
        Cart.Item item = new Cart.Item();
        item.setProductId(product.getId());
        item.setName(product.getName());
        item.setUnit(product.getUnit());
        item.setPrice(product.getPrice());
        item.setQuantity(2);
        cart.getItems().add(item);

        CheckoutForm checkout = new CheckoutForm();
        checkout.setFulfillment(Enums.Fulfillment.DELIVERY);
        checkout.setPhone("0909999999");
        checkout.setAddress("12 Nguyễn Trãi");
        checkout.setIdempotencyKey(UUID.randomUUID().toString());
        checkout.setLat(10.7725);
        checkout.setLng(106.6980);

        Order order = orders.placeOrder(customer, cart, checkout);
        assertThat(order.getStatus()).isEqualTo(Enums.OrderStatus.PENDING);
        assertThat(order.getTotal()).isEqualTo(80000);

        orders.transition(order, Enums.OrderStatus.CONFIRMED, owner, "confirm");
        assertThat(products.findById(product.getId()).orElseThrow().getStock()).isEqualTo(8);

        orders.transition(order, Enums.OrderStatus.PREPARING, owner, null);
        orders.transition(order, Enums.OrderStatus.OUT_FOR_DELIVERY, owner, null);
        Order completed = orders.transition(order, Enums.OrderStatus.COMPLETED, owner, null);
        assertThat(completed.getStatus()).isEqualTo(Enums.OrderStatus.COMPLETED);
        assertThat(completed.isPaid()).isTrue();
    }

    @Test
    void customerCanOpenStoreAndUseOwnerAreaWithoutRelogin() throws Exception {
        User newOwner = users.provision("new-owner@example.com", "Khách mở tiệm", null);

        mvc.perform(post("/store/profile")
                        .with(user(newOwner.getEmail()).roles("CUSTOMER"))
                        .with(csrf())
                        .param("name", "Tiệm mới")
                        .param("address", "2 Test, Phường Bến Thành")
                        .param("phone", "0901234568")
                        .param("radiusKm", "1.5")
                        .param("deliveryEnabled", "true")
                        .param("pickupEnabled", "true")
                        .param("deliveryFee", "10000")
                        .param("minOrder", "0")
                        .param("openMinute", "420")
                        .param("closeMinute", "1320"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/store"));

        Store created = storeRepository.findByOwnerId(newOwner.getId()).orElseThrow();
        assertThat(created.getName()).isEqualTo("Tiệm mới");
        assertThat(userRepository.findByEmail(newOwner.getEmail()).orElseThrow()
                .hasRole(Enums.Role.STORE_OWNER)).isTrue();

        mvc.perform(get("/store").with(user(newOwner.getEmail()).roles("CUSTOMER")))
                .andExpect(status().isOk());
    }

    @Test
    void onlineOrderCannotBeConfirmedBeforePayment() {
        Cart cart = new Cart();
        cart.setStoreId(store.getId());
        Cart.Item item = new Cart.Item();
        item.setProductId(product.getId());
        item.setName(product.getName());
        item.setUnit(product.getUnit());
        item.setPrice(product.getPrice());
        item.setQuantity(1);
        cart.getItems().add(item);

        CheckoutForm checkout = new CheckoutForm();
        checkout.setFulfillment(Enums.Fulfillment.DELIVERY);
        checkout.setPaymentMethod(Enums.PaymentMethod.PAYOS);
        checkout.setPhone("0909999998");
        checkout.setAddress("14 Nguyễn Trãi");
        checkout.setIdempotencyKey(UUID.randomUUID().toString());
        checkout.setLat(10.7725);
        checkout.setLng(106.6980);

        Order order = orders.placeOrder(customer, cart, checkout);
        assertThat(order.getPaymentMethod()).isEqualTo(Enums.PaymentMethod.PAYOS);
        assertThat(order.getPaymentStatus()).isEqualTo(Enums.PaymentStatus.UNPAID);
        assertThatThrownBy(() -> orders.transition(order, Enums.OrderStatus.CONFIRMED, owner, null))
                .isInstanceOf(vn.taphoagan.web.BusinessException.class)
                .hasMessageContaining("chưa thanh toán");

        orders.markPaidOnline(order.getId(), order.getTotal(), "PAYOS-TEST", "LINK-TEST");
        Order paid = orders.transition(order, Enums.OrderStatus.CONFIRMED, owner, null);
        assertThat(paid.isPaid()).isTrue();
        assertThat(paid.getPaymentStatus()).isEqualTo(Enums.PaymentStatus.PAID);
    }

    @Test
    void storeCanSaveOvernightHours() throws Exception {
        User newOwner = users.provision("overnight-owner@example.com", "Tiệm qua đêm", null);
        mvc.perform(post("/store/profile")
                        .with(user(newOwner.getEmail()).roles("CUSTOMER"))
                        .with(csrf())
                        .param("name", "Tiệm qua đêm")
                        .param("address", "3 Test, Phường Bến Thành")
                        .param("phone", "0901234569")
                        .param("radiusKm", "1.5")
                        .param("deliveryEnabled", "true")
                        .param("pickupEnabled", "true")
                        .param("deliveryFee", "10000")
                        .param("minOrder", "0")
                        .param("openMinute", "1320")
                        .param("closeMinute", "360"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/store"));

        Store created = storeRepository.findByOwnerId(newOwner.getId()).orElseThrow();
        StoreForm saved = storeService.toForm(created);
        assertThat(saved.getOpenMinute()).isEqualTo(1320);
        assertThat(saved.getCloseMinute()).isEqualTo(360);
    }

    @Test
    void accessControlReflectsRolesAndOwnership() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk());
        mvc.perform(get("/store"))
                .andExpect(status().is3xxRedirection());
        mvc.perform(get("/store").with(user("customer-test@example.com").roles("CUSTOMER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/store/profile"));
        mvc.perform(get("/store/profile").with(user("customer-test@example.com").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(view().name("store/profile"));
        mvc.perform(get("/store").with(user("owner-test@example.com").roles("STORE_OWNER")))
                .andExpect(status().isOk());
        mvc.perform(get("/orders"))
                .andExpect(status().is3xxRedirection());
        mvc.perform(get("/orders").with(user("customer-test@example.com").roles("CUSTOMER")))
                .andExpect(status().isOk());
    }
}
