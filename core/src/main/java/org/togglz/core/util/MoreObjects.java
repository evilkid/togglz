/*
 * Copyright (C) 2007 The Guava Authors
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

package org.togglz.core.util;

import java.util.Arrays;

import static org.togglz.core.util.Preconditions.checkNotNull;

/**
 * Helper functions that can operate on any {@code Object}.
 * <p>
 * <p>See the Guava User Guide on <a href="http://code.google.com/p/guava-libraries/wiki/CommonObjectUtilitiesExplained">writing
 * {@code Object} methods with {@code Objects}</a>.
 * <p>
 * <p>Imported from com.google.guava:17.0 Objects
 *
 * @author Laurence Gonsalves
 */
public final class MoreObjects {

    private MoreObjects() {
    }

    /**
     * Creates an instance of {@link ToStringHelper}.
     * <p>
     * <p>This is helpful for implementing {@link Object#toString()}.
     * Specification by example: <pre>   {@code
     *   // Returns "ClassName{}"
     *   Objects.toStringHelper(this)
     *       .toString();
     * <p>
     *   // Returns "ClassName{x=1}"
     *   Objects.toStringHelper(this)
     *       .add("x", 1)
     *       .toString();
     * <p>
     *   // Returns "MyObject{x=1}"
     *   Objects.toStringHelper("MyObject")
     *       .add("x", 1)
     *       .toString();
     * <p>
     *   // Returns "ClassName{x=1, y=foo}"
     *   Objects.toStringHelper(this)
     *       .add("x", 1)
     *       .add("y", "foo")
     *       .toString();
     * <p>
     *   // Returns "ClassName{x=1}"
     *   Objects.toStringHelper(this)
     *       .omitNullValues()
     *       .add("x", 1)
     *       .add("y", null)
     *       .toString();
     *   }}</pre>
     * <p>
     * <p>Note that in GWT, class names are often obfuscated.
     *
     * @param self the object to generate the string for (typically {@code this}),
     *             used only for its class name
     * @since 2.0
     */
    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(simpleName(self.getClass()));
    }

    /**
     * {@link Class#getSimpleName()} is not GWT compatible yet, so we
     * provide our own implementation.
     */
    private static String simpleName(Class<?> clazz) {
        String name = clazz.getName();

        // the nth anonymous class has a class name ending in "Outer$n"
        // and local inner classes have names ending in "Outer.$1Inner"
        name = name.replaceAll("\\$[0-9]+", "\\$");

        // we want the name of the inner class all by its lonesome
        int start = name.lastIndexOf('$');

        // if this isn't an inner class, just find the start of the
        // top level class name.
        if (start == -1) {
            start = name.lastIndexOf('.');
        }
        return name.substring(start + 1);
    }

    /**
     * Returns the first of two given parameters that is not {@code null}, if
     * either is, or otherwise throws a {@link NullPointerException}.
     *
     * @return {@code first} if {@code first} is not {@code null}, or
     * {@code second} if {@code first} is {@code null} and {@code second} is
     * not {@code null}
     * @throws NullPointerException if both {@code first} and {@code second} were
     *                              {@code null}
     * @since 3.0
     */
    public static <T> T firstNonNull(T first, T second) {
        return first != null ? first : checkNotNull(second);
    }

    /**
     * Support class for {@link MoreObjects#toStringHelper}.
     *
     * @author Jason Lee
     * @since 2.0
     */
    public static final class ToStringHelper {
        private final String className;
        private ValueHolder holderHead = new ValueHolder();
        private ValueHolder holderTail = holderHead;
        private boolean omitNullValues = false;

        /**
         * Use {@link MoreObjects#toStringHelper(Object)} to create an instance.
         */
        private ToStringHelper(String className) {
            this.className = checkNotNull(className);
        }

        /**
         * Configures the {@link ToStringHelper} so {@link #toString()} will ignore
         * properties with null value. The order of calling this method, relative
         * to the {@code add()}/{@code addValue()} methods, is not significant.
         *
         * @since 12.0
         */
        public ToStringHelper omitNullValues() {
            omitNullValues = true;
            return this;
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format. If {@code value} is {@code null}, the string {@code "null"}
         * is used, unless {@link #omitNullValues()} is called, in which case this
         * name/value pair will not be added.
         */
        public ToStringHelper add(String name, Object value) {
            return addHolder(name, value);
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format.
         *
         * @since 11.0 (source-compatible since 2.0)
         */
        public ToStringHelper add(String name, boolean value) {
            return addHolder(name, String.valueOf(value));
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format.
         *
         * @since 11.0 (source-compatible since 2.0)
         */
        public ToStringHelper add(String name, char value) {
            return addHolder(name, String.valueOf(value));
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format.
         *
         * @since 11.0 (source-compatible since 2.0)
         */
        public ToStringHelper add(String name, double value) {
            return addHolder(name, String.valueOf(value));
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format.
         *
         * @since 11.0 (source-compatible since 2.0)
         */
        public ToStringHelper add(String name, float value) {
            return addHolder(name, String.valueOf(value));
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format.
         *
         * @since 11.0 (source-compatible since 2.0)
         */
        public ToStringHelper add(String name, int value) {
            return addHolder(name, String.valueOf(value));
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format.
         *
         * @since 11.0 (source-compatible since 2.0)
         */
        public ToStringHelper add(String name, long value) {
            return addHolder(name, String.valueOf(value));
        }

        /**
         * Returns a string in the format specified by {@link
         * MoreObjects#toStringHelper(Object)}.
         * <p>
         * <p>After calling this method, you can keep adding more properties to later
         * call toString() again and get a more complete representation of the
         * same object; but properties cannot be removed, so this only allows
         * limited reuse of the helper instance. The helper allows duplication of
         * properties (multiple name/value pairs with the same name can be added).
         */
        @Override
        public String toString() {
            // create a copy to keep it consistent in case value changes
            boolean omitNullValuesSnapshot = omitNullValues;
            String nextSeparator = "";
            StringBuilder builder = new StringBuilder(32).append(className)
                    .append('{');
            for (ValueHolder valueHolder = holderHead.next; valueHolder != null;
                 valueHolder = valueHolder.next) {
                if (!omitNullValuesSnapshot || valueHolder.value != null) {
                    builder.append(nextSeparator);
                    nextSeparator = ", ";

                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=');
                    }
                    builder.append(valueHolder.value);
                }
            }
            return builder.append('}').toString();
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            holderTail = holderTail.next = valueHolder;
            return valueHolder;
        }

        private ToStringHelper addHolder(Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            return this;
        }

        private ToStringHelper addHolder(String name, Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            valueHolder.name = checkNotNull(name);
            return this;
        }

        private static final class ValueHolder {
            String name;
            Object value;
            ValueHolder next;
        }
    }
}
