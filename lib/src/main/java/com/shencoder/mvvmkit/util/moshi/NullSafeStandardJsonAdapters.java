package com.shencoder.mvvmkit.util.moshi;


import androidx.annotation.NonNull;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.internal.Util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @date 2023/08/01 22:10
 */
public final class NullSafeStandardJsonAdapters {
    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        public JsonAdapter<?> create(@NonNull Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (!annotations.isEmpty()) {
                return null;
            } else if (type == Boolean.TYPE) {
                return BOOLEAN_JSON_ADAPTER;
            } else if (type == Byte.TYPE) {
                return BYTE_JSON_ADAPTER;
            } else if (type == Character.TYPE) {
                return CHARACTER_JSON_ADAPTER;
            } else if (type == Double.TYPE) {
                return DOUBLE_JSON_ADAPTER;
            } else if (type == Float.TYPE) {
                return FLOAT_JSON_ADAPTER;
            } else if (type == Integer.TYPE) {
                return INTEGER_JSON_ADAPTER;
            } else if (type == Long.TYPE) {
                return LONG_JSON_ADAPTER;
            } else if (type == Short.TYPE) {
                return SHORT_JSON_ADAPTER;
            } else if (type == Boolean.class) {
                return BOOLEAN_JSON_ADAPTER;
            } else if (type == Byte.class) {
                return BYTE_JSON_ADAPTER;
            } else if (type == Character.class) {
                return CHARACTER_JSON_ADAPTER;
            } else if (type == Double.class) {
                return DOUBLE_JSON_ADAPTER;
            } else if (type == Float.class) {
                return FLOAT_JSON_ADAPTER;
            } else if (type == Integer.class) {
                return INTEGER_JSON_ADAPTER;
            } else if (type == Long.class) {
                return LONG_JSON_ADAPTER;
            } else if (type == Short.class) {
                return SHORT_JSON_ADAPTER;
            } else if (type == String.class) {
                return STRING_JSON_ADAPTER;
            } else if (type == Object.class) {
                return (new ObjectJsonAdapter(moshi)).nullSafe();
            } else {
                Class<?> rawType = Types.getRawType(type);
                JsonAdapter<?> generatedAdapter = Util.generatedAdapter(moshi, type, rawType);
                if (generatedAdapter != null) {
                    return generatedAdapter;
                } else {
                    return rawType.isEnum() ? (new EnumJsonAdapter(rawType)).nullSafe() : null;
                }
            }
        }
    };
    private static final String ERROR_FORMAT = "Expected %s but was %s at path %s";
    static final JsonAdapter<Boolean> BOOLEAN_JSON_ADAPTER = new JsonAdapter<Boolean>() {
        public Boolean fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return false;
            }
            return reader.nextBoolean();

        }

        public void toJson(JsonWriter writer, Boolean value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);

        }


        @Override
        public String toString() {
            return "JsonAdapter(Boolean)";
        }
    };

    static final JsonAdapter<Byte> BYTE_JSON_ADAPTER = new JsonAdapter<Byte>() {
        @Override
        public Byte fromJson(JsonReader reader) throws IOException {
            return (byte) rangeCheckNextInt(reader, "a byte", -128, 255);
        }

        @Override
        public void toJson(JsonWriter writer, Byte value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            int intValue;
            intValue = value.intValue();
            writer.value((long) (intValue & 255));
        }


        @Override
        public String toString() {
            return "JsonAdapter(Byte)";
        }
    };

    static final JsonAdapter<Character> CHARACTER_JSON_ADAPTER = new JsonAdapter<Character>() {
        @Override
        public Character fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return Character.MIN_VALUE;
            }
            String value = reader.nextString();
            if (value.length() > 1) {
                throw new JsonDataException(
                        String.format(ERROR_FORMAT, "a char", '"' + value + '"', reader.getPath()));
            }
            return value.charAt(0);

        }

        @Override
        public void toJson(JsonWriter writer, Character value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value.toString());
        }


        @Override
        public String toString() {
            return "JsonAdapter(Character)";
        }
    };

    static final JsonAdapter<Double> DOUBLE_JSON_ADAPTER = new JsonAdapter<Double>() {
        @Override
        public Double fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return 0.0;
            }
            return reader.nextDouble();

        }

        @Override
        public void toJson(JsonWriter writer, Double value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }


        @Override
        public String toString() {
            return "JsonAdapter(Double)";
        }
    };

    static final JsonAdapter<Float> FLOAT_JSON_ADAPTER = new JsonAdapter<Float>() {
        @Override
        public Float fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return 0f;
            } else {
                float value = (float) reader.nextDouble();
                if (!reader.isLenient() && Float.isInfinite(value)) {
                    throw new JsonDataException("JSON forbids NaN and infinities: " + value + " at path " + reader.getPath());
                }
                return value;

            }
        }

        @Override
        public void toJson(JsonWriter writer, Float value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }


        @Override
        public String toString() {
            return "JsonAdapter(Float)";
        }
    };

    static final JsonAdapter<Integer> INTEGER_JSON_ADAPTER = new JsonAdapter<Integer>() {
        @Override
        public Integer fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return 0;
            }
            return reader.nextInt();

        }

        @Override
        public void toJson(JsonWriter writer, Integer value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value((long) value);
        }


        @Override
        public String toString() {
            return "JsonAdapter(Integer)";
        }
    };

    static final JsonAdapter<Long> LONG_JSON_ADAPTER = new JsonAdapter<Long>() {
        @Override
        public Long fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return 0L;
            }
            return reader.nextLong();
        }

        @Override
        public void toJson(JsonWriter writer, Long value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }


        @Override
        public String toString() {
            return "JsonAdapter(Long)";
        }
    };

    static final JsonAdapter<Short> SHORT_JSON_ADAPTER = new JsonAdapter<Short>() {
        @Override
        public Short fromJson(JsonReader reader) throws IOException {
            return (short) rangeCheckNextInt(reader, "a short", -32768, 32767);
        }

        @Override
        public void toJson(JsonWriter writer, Short value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value((long) value.intValue());
        }


        @Override
        public String toString() {
            return "JsonAdapter(Short)";
        }
    };
    static final JsonAdapter<String> STRING_JSON_ADAPTER = new JsonAdapter<String>() {
        public String fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void toJson(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }

        public String toString() {
            return "JsonAdapter(String)";
        }
    };

    private NullSafeStandardJsonAdapters() {
    }

    static int rangeCheckNextInt(JsonReader reader, String typeMessage, int min, int max) throws IOException {
        int value;
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull();
            value = 0;
        } else {
            value = reader.nextInt();
        }
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new JsonDataException(String.format("Expected %s but was %s at path %s", typeMessage, value, reader.getPath()));
        }
    }

    static final class ObjectJsonAdapter extends JsonAdapter<Object> {
        private final Moshi moshi;
        private final JsonAdapter<List> listJsonAdapter;
        private final JsonAdapter<Map> mapAdapter;
        private final JsonAdapter<String> stringAdapter;
        private final JsonAdapter<Double> doubleAdapter;
        private final JsonAdapter<Boolean> booleanAdapter;

        ObjectJsonAdapter(Moshi moshi) {
            this.moshi = moshi;
            this.listJsonAdapter = moshi.adapter(List.class);
            this.mapAdapter = moshi.adapter(Map.class);
            this.stringAdapter = moshi.adapter(String.class);
            this.doubleAdapter = moshi.adapter(Double.class);
            this.booleanAdapter = moshi.adapter(Boolean.class);
        }

        public Object fromJson(JsonReader reader) throws IOException {
            return switch (reader.peek()) {
                case BEGIN_ARRAY -> this.listJsonAdapter.fromJson(reader);
                case BEGIN_OBJECT -> this.mapAdapter.fromJson(reader);
                case STRING -> this.stringAdapter.fromJson(reader);
                case NUMBER -> this.doubleAdapter.fromJson(reader);
                case BOOLEAN -> this.booleanAdapter.fromJson(reader);
                case NULL -> reader.nextNull();
                default ->
                        throw new IllegalStateException("Expected a value but was " + reader.peek() + " at path " + reader.getPath());
            };
        }

        public void toJson(JsonWriter writer, Object value) throws IOException {
            Class<?> valueClass = value.getClass();
            if (valueClass == Object.class) {
                writer.beginObject();
                writer.endObject();
            } else {
                this.moshi.adapter(this.toJsonType(valueClass), Util.NO_ANNOTATIONS).toJson(writer, value);
            }

        }

        private Class<?> toJsonType(Class<?> valueClass) {
            if (Map.class.isAssignableFrom(valueClass)) {
                return Map.class;
            } else {
                return Collection.class.isAssignableFrom(valueClass) ? Collection.class : valueClass;
            }
        }

        public String toString() {
            return "JsonAdapter(Object)";
        }
    }

    static final class EnumJsonAdapter<T extends Enum<T>> extends JsonAdapter<T> {
        private final Class<T> enumType;
        private final String[] nameStrings;
        private final T[] constants;
        private final JsonReader.Options options;

        EnumJsonAdapter(Class<T> enumType) {
            this.enumType = enumType;

            try {
                this.constants = (T[]) enumType.getEnumConstants();
                this.nameStrings = new String[this.constants.length];

                for (int i = 0; i < this.constants.length; ++i) {
                    T constant = this.constants[i];
                    String constantName = constant.name();
                    this.nameStrings[i] = Util.jsonName(constantName, enumType.getField(constantName));
                }

                this.options = JsonReader.Options.of(this.nameStrings);
            } catch (NoSuchFieldException var5) {
                throw new AssertionError("Missing field in " + enumType.getName(), var5);
            }
        }

        public T fromJson(JsonReader reader) throws IOException {
            int index = reader.selectString(this.options);
            if (index != -1) {
                return this.constants[index];
            } else {
                String path = reader.getPath();
                String name = reader.nextString();
                throw new JsonDataException("Expected one of " + Arrays.asList(this.nameStrings) + " but was " + name + " at path " + path);
            }
        }

        public void toJson(JsonWriter writer, T value) throws IOException {
            writer.value(this.nameStrings[value.ordinal()]);
        }

        public String toString() {
            return "JsonAdapter(" + this.enumType.getName() + ")";
        }
    }
}
