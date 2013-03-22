/*
 * Copyright (C) 2013 jtervoorde.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.deegree.commons.concurrent;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jtervoorde
 */
public class ExecutionContext extends HashMap<String, Object> {
    public static ThreadLocal<ExecutionContext> CURRENT = new ThreadLocal<ExecutionContext>();

    public static void init(Map<String, Object> values) {
        CURRENT.set(new ExecutionContext(values));
    }
    
    public static void init() {
        init(new HashMap<String, Object>());
    }

    public static void dispose() {
        CURRENT.set(null);
    }
    
    public static ExecutionContext getCurrent() {
        return CURRENT.get();
    }
    
    public ExecutionContext(Map<String, Object> other) {
        super(other);
    }
}
