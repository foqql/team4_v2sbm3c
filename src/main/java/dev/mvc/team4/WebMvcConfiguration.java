package dev.mvc.team4;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.exchange.Exchange;
import dev.mvc.gallery.Gallery;
import dev.mvc.news.News;
import dev.mvc.weather.Weather;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Windows: path = "C:/kd/deploy/vitamin/exchange/storage";
    // ▶ file:///C:/kd/deploy/resort/exchange/storage

    // Ubuntu: path = "/home/ubuntu/deploy/vitamin/exchange/storage";
    // ▶ file:////home/ubuntu/deploy/vitamin/exchange/storage

    // JSP 인식되는 경로: http://localhost:9091/exchange/storage";
    registry.addResourceHandler("/news/storage/**").addResourceLocations("file:///" + News.getUploadDir());
    registry.addResourceHandler("/exchange/storage/**").addResourceLocations("file:///" + Exchange.getUploadDir());
    registry.addResourceHandler("/weather/storage/**").addResourceLocations("file:///" + Weather.getUploadDir());
    registry.addResourceHandler("/gallery/storage/**").addResourceLocations("file:///" + Gallery.getUploadDir());
    registry.addResourceHandler("/uploads/**") .addResourceLocations("file:///C:/uploads/");

    // JSP 인식되는 경로: http://localhost:9091/food/storage";
    // registry.addResourceHandler("/exchange/storage/**").addResourceLocations("file:///"
    // + Food.getUploadDir());

    // JSP 인식되는 경로: http://localhost:9091/tirp/storage";
    // registry.addResourceHandler("/exchange/storage/**").addResourceLocations("file:///"
    // + Trip.getUploadDir());

  }

}
