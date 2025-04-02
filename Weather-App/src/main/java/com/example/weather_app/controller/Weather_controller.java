package com.example.weather_app.controller;

//import ch.qos.logback.core.model.Model;
import com.example.weather_app.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;

@Controller//to make the weather controller a springboot controller we use this that would handle all the http calls that would be made from the http template that we have in index.html and weather.html
public class Weather_controller {
    @Value("${api.key}")//takes the value from application.properties
    private String apiKey;
@GetMapping("/")//defines mapping for a get call followed by the path in the small brackets which is "/"
    public String getIndex(){ //we define a method getIndex which will get called whenever there will be a call in the GetMapping("/")
    return "index";
}
    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model){
        //Calling open weather API:-
        String url="http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appID=" + apiKey + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();//Used to make the http request to this web api
        //to store the response back from this web api we need to create yet another class with the name:-
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);
        System.out.println("API Response: "+weatherResponse);
        if (weatherResponse != null) {
            model.addAttribute("city", weatherResponse.getName());
            model.addAttribute("country", weatherResponse.getSys().getCountry());

            if (weatherResponse.getWeather() != null && !weatherResponse.getWeather().isEmpty()) {
                model.addAttribute("weatherDescription", weatherResponse.getWeather().get(0).getDescription());
            } else {
                model.addAttribute("weatherDescription", "N/A");
            }

            if (weatherResponse.getMain() != null) {
                model.addAttribute("temperature", weatherResponse.getMain().getTemp());
                model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
            } else {
                model.addAttribute("humidity", "N/A");
            }

            if (weatherResponse.getWind() != null) {
                model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());
            } else {
                model.addAttribute("windSpeed", "N/A");
            }
        }
        return "weather";
    }
}
