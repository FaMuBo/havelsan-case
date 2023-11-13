package com.havelsan.backend.controller;

import com.havelsan.backend.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping("/random/position")
    public ResponseEntity<Map<String, Object>> randomMarkerPosition(@RequestParam Integer zoom){
        return ResponseEntity.ok(this.mapService.randomMarkerPosition(zoom));
    }

}
