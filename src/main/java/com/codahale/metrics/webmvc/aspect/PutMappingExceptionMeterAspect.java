/*
 * Copyright (c) 2018 (https://github.com/hiwepy).
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
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.bind.annotation.PutMapping;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.webmvc.utils.MetricUtils;

public class PutMappingExceptionMeterAspect extends AbstractMetricMethodAspect<PutMapping, Meter> implements Ordered {

	public static final Class<PutMapping> ANNOTATION = PutMapping.class;
	public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, ANNOTATION);
	public static final MethodFilter METHOD_FILTER = new AnnotationFilter(ANNOTATION, AnnotationFilter.PROXYABLE_METHODS);

	public PutMappingExceptionMeterAspect(final MetricRegistry metricRegistry) {
		this(metricRegistry, false);
	}
	
	public PutMappingExceptionMeterAspect(final MetricRegistry metricRegistry, boolean absolute) {
		super(metricRegistry, absolute, ANNOTATION, METHOD_FILTER);
	}

	@Override
	protected Object invoke(ProceedingJoinPoint joinPoint, Meter meter, PutMapping annotation) throws Throwable {
		try {
			return joinPoint.proceed();
		}
		catch (Throwable t) {
			meter.mark();
			throw t;
		}
	}

	@Override
	protected Meter buildMetric(MetricRegistry metricRegistry, String metricName, PutMapping annotation) {
		return metricRegistry.meter(metricName);
	}

	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, PutMapping annotation, boolean absolute) {
		return MetricUtils.forMetricMethod(targetClass, method, annotation, absolute);
	}
	
	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
