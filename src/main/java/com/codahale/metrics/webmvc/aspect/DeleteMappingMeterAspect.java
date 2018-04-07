/*
 * Copyright (c) 2018 (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codahale.metrics.webmvc.aspect;


import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.webmvc.utils.MetricUtils;

public class DeleteMappingMeterAspect extends AbstractMetricMethodAspect<DeleteMapping, Meter> {

	public static final Class<DeleteMapping> ANNOTATION = DeleteMapping.class;
	public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, ANNOTATION);
	public static final MethodFilter METHOD_FILTER = new AnnotationFilter(ANNOTATION, AnnotationFilter.PROXYABLE_METHODS);
	
	public DeleteMappingMeterAspect(final MetricRegistry metricRegistry) {
		this(metricRegistry, false);
	}
	
	public DeleteMappingMeterAspect(final MetricRegistry metricRegistry, boolean absolute) {
		super(metricRegistry, absolute, ANNOTATION, METHOD_FILTER);
	}

	@Override
	protected Object invoke(ProceedingJoinPoint joinPoint, Meter meter, DeleteMapping annotation) throws Throwable {
		meter.mark();
		return joinPoint.proceed();
	}

	@Override
	protected Meter buildMetric(MetricRegistry metricRegistry, String metricName, DeleteMapping annotation) {
		return metricRegistry.meter(metricName);
	}

	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, DeleteMapping annotation, boolean absolute) {
		return MetricUtils.forMetricMethod(targetClass, method, annotation, absolute);
	}
	

}
