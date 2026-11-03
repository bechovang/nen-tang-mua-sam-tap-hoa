package vn.taphoagan.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.taphoagan.repository.WardRepository;
import vn.taphoagan.service.DiscoveryService;
import vn.taphoagan.service.GeoService;
import vn.taphoagan.web.dto.DiscoveryViews;
import vn.taphoagan.web.form.ActionForms;

import java.util.List;

@Controller
public class HomeController {

    private final DiscoveryService discovery;
    private final WardRepository wards;
    private final GeoService geo;
    private final vn.taphoagan.service.StoreService storeService;

    public HomeController(DiscoveryService discovery, WardRepository wards, GeoService geo,
                          vn.taphoagan.service.StoreService storeService) {
        this.discovery = discovery;
        this.wards = wards;
        this.geo = geo;
        this.storeService = storeService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Double lat = (Double) session.getAttribute("lat");
        Double lng = (Double) session.getAttribute("lng");
        model.addAttribute("wards", wards.findAllByOrderByNameAsc());
        model.addAttribute("lat", lat);
        model.addAttribute("lng", lng);
        model.addAttribute("featuredStores", discovery.featuredStores(lat, lng, 3));
        model.addAttribute("featuredProducts", discovery.featuredProducts(6));
        if (lat != null && lng != null) {
            List<DiscoveryViews.NearbyStore> stores = discovery.nearby(lat, lng);
            model.addAttribute("stores", stores);
            model.addAttribute("distanceFormatter", geo);
        }
        return "home";
    }

    @GetMapping("/browse")
    public String browse(@RequestParam(defaultValue = "") String q,
                         @RequestParam(required = false) Long wardId,
                         @RequestParam(defaultValue = "5") double radiusKm,
                         @RequestParam(defaultValue = "false") boolean openNow,
                         @RequestParam(defaultValue = "false") boolean delivery,
                         @RequestParam(defaultValue = "false") boolean pickup,
                         @RequestParam(defaultValue = "distance") String sort,
                         HttpSession session, Model model) {
        Double lat = (Double) session.getAttribute("lat");
        Double lng = (Double) session.getAttribute("lng");
        model.addAttribute("q", q);
        model.addAttribute("wardId", wardId);
        model.addAttribute("radiusKm", radiusKm);
        model.addAttribute("openNow", openNow);
        model.addAttribute("delivery", delivery);
        model.addAttribute("pickup", pickup);
        model.addAttribute("sort", sort);
        model.addAttribute("wards", wards.findAllByOrderByNameAsc());
        model.addAttribute("lat", lat);
        model.addAttribute("lng", lng);
        model.addAttribute("wardName", session.getAttribute("wardName"));
        model.addAttribute("distanceFormatter", geo);
        model.addAttribute("stores", discovery.browse(q, wardId, lat, lng, radiusKm, openNow, delivery, pickup, sort));
        return "browse";
    }

    @GetMapping("/location/reset")
    public String resetLocation(HttpSession session) {
        session.removeAttribute("lat");
        session.removeAttribute("lng");
        session.removeAttribute("wardName");
        return "redirect:/";
    }

    @PostMapping("/location")
    public String location(@ModelAttribute("locationForm") ActionForms.LocationForm form, HttpSession session) {
        if (form.getWardId() != null) {
            wards.findById(form.getWardId()).ifPresent(ward -> {
                session.setAttribute("lat", ward.getLat());
                session.setAttribute("lng", ward.getLng());
                session.setAttribute("wardName", ward.getName());
            });
        } else if (form.getLat() != null && form.getLng() != null) {
            session.setAttribute("lat", form.getLat());
            session.setAttribute("lng", form.getLng());
            session.setAttribute("wardName", "Vị trí hiện tại");
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, HttpSession session, Model model) {
        Double lat = (Double) session.getAttribute("lat");
        Double lng = (Double) session.getAttribute("lng");
        if (lat == null || lng == null) {
            lat = 10.776;
            lng = 106.700;
        }
        model.addAttribute("q", q);
        model.addAttribute("results", q.isBlank() ? List.of() : discovery.search(q, lat, lng));
        model.addAttribute("distanceFormatter", geo);
        return "search";
    }

    @GetMapping("/stores/{slug}")
    public String store(@PathVariable String slug, HttpSession session, Model model) {
        Double lat = (Double) session.getAttribute("lat");
        Double lng = (Double) session.getAttribute("lng");
        DiscoveryViews.StorePage page = discovery.storePage(slug, lat, lng);
        model.addAttribute("store", page.store());
        model.addAttribute("products", page.products());
        model.addAttribute("distanceKm", page.distanceKm());
        model.addAttribute("openNow", storeService.isOpenNow(page.store()));
        model.addAttribute("distanceFormatter", geo);
        return "store-public";
    }
}
