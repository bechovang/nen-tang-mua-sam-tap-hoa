package vn.taphoagan.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.taphoagan.service.BarcodeLookupService;

@RestController
@RequestMapping("/api")
public class ScanApiController {

    private final BarcodeLookupService lookup;

    public ScanApiController(BarcodeLookupService lookup) {
        this.lookup = lookup;
    }

    @GetMapping("/barcode/{barcode}")
    public BarcodeLookupService.BarcodeResult lookup(@PathVariable String barcode) {
        return lookup.lookup(barcode);
    }
}
