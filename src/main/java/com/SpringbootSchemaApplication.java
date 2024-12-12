package com;

import com.utils.RecommendUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.List;

@SpringBootApplication
@MapperScan(basePackages = {"com.dao"})
public class SpringbootSchemaApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		double[][] userRatings = {
				{1, 0, 0, 1,0,1},
				{1, 1, 1, 1,1,1},
				{1, 1, 0, 1,0,1},
				{1, 0, 0, 1,0,1},
				{1, 1, 1, 1,0,1},
		};
	List<Integer> asd=RecommendUtils.recommendItems(0, userRatings, 3);

		SpringApplication.run(SpringbootSchemaApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(SpringbootSchemaApplication.class);
    }
}
