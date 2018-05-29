package com.alphacat;

import java.util.List;

import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Configuration
public class BeanConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter fsjsConverter = 
			new FastJsonHttpMessageConverter();
		FastJsonConfig fsjsConfig = new FastJsonConfig();
		// set up features
		fsjsConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		
		fsjsConverter.setFastJsonConfig(fsjsConfig);
		converters.add(fsjsConverter);
	}

	@Bean
	public Mapper getMapper() {
	    return DozerBeanMapperBuilder.create()
                .withMappingFiles("config/dozer-mapping.xml")
                .build();
    }

}
