package com.turkeycrew;

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


}
