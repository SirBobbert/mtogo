package com.turkeycrew;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/getDeliveries")
    public void test() {
        dashboardService.getDeliveries();
    }

    @GetMapping("/getOrders")
    public void getOrders() {
        dashboardService.getOrders();
    }

    @GetMapping("/getRestaurants")
    public void getRestaurants() {
        dashboardService.getRestaurants();
    }

    @GetMapping("/getFeedback")
    public void getFeedback() {
        dashboardService.getFeedback();
    }

    @GetMapping("/getAll")
    public ResponseEntity<String> getAll() {
        return dashboardService.getAllData();
    }

    @GetMapping("/averagePriceByCityAreaRestaurant")
    public ResponseEntity<String> averagePriceByCityAreaRestaurant() {
        return dashboardService.averagePriceByCityAreaRestaurant();
    }

    @GetMapping("/averageRatingByCityAreaRestaurant")
    public ResponseEntity<String> averageRatingByCityAreaRestaurant() {
        return dashboardService.averageRatingByCityAreaRestaurant();
    }

    @GetMapping("/distributionOfRatings")
    public ResponseEntity<String> distributionOfRatings() {
        return dashboardService.distributionOfRatings();
    }
    @GetMapping("/mostPopularFoodTypes")
    public ResponseEntity<String> mostPopularFoodTypes() {
        return dashboardService.mostPopularFoodTypes();
    }

    @GetMapping("deliveryTimeAnalysis")
    public ResponseEntity<String> deliveryTimeAnalysis() {
        return dashboardService.deliveryTimeAnalysis();
    }
    @GetMapping("/topRestaurants")
    public ResponseEntity<String> topRestaurants() {
        return dashboardService.topRestaurants();
    }
    @GetMapping("/foodTypePreferences")
    public ResponseEntity<String> foodTypePreferences() {
        return dashboardService.foodTypePreferences();
    }
    @GetMapping("/averageTotalRatingsByCityAreaRestaurant")
    public ResponseEntity<String> averageTotalRatingsByCityAreaRestaurant() {
        return dashboardService.averageTotalRatingsByCityAreaRestaurant();
    }
    @GetMapping("/averageDeliveryTimeByFoodType")
    public ResponseEntity<String> averageDeliveryTimeByFoodType() {
        return dashboardService.averageDeliveryTimeByFoodType();
    }
}
