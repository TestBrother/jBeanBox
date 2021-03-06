/**
 * Copyright (C) 2016 Yong Zhu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.drinkjava2.jbeanbox;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/**
 * MethodInvocation interface detail see AOP alliance doc, here is a brief implementation
 * 
 * @author Yong Zhu
 * @since 2.4
 *
 */
class AopAllianceInvocation implements MethodInvocation {
	private final Object target;
	private final Method method;
	private Object[] arguments;
	private final AdviceCaller caller;

	protected AopAllianceInvocation(Object target, Method method, Object[] arguments, AdviceCaller caller) {
		this.target = target;
		this.method = method;
		this.arguments = arguments;
		this.caller = caller;
	}

	@Override
	public final Object getThis() {
		return this.target;
	}

	@Override
	public final AccessibleObject getStaticPart() {
		return this.method;
	}

	@Override
	public final Method getMethod() {
		return this.method;
	}

	@Override
	public final Object[] getArguments() {
		return this.arguments != null ? this.arguments : new Object[0];
	}

	@Override
	public Object proceed() throws Throwable {
		return caller.callNextAdvisor();
	}
}