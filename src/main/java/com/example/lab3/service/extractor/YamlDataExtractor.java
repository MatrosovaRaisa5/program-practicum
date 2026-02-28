package com.example.lab3.service.extractor;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import java.util.List;
import java.util.Map;

@Component("yaml")
public class YamlDataExtractor implements DataExtractor {

    private final Yaml yaml = new Yaml();

    @Override
    public String extract(String data, String path) throws Exception {
        Map<String, Object> root = yaml.load(data);
        String[] parts = path.split("/");
        Object current = root;

        for (String part : parts) {
            if (part.isEmpty()) continue;

            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else if (current instanceof List) {
                try {
                    int index = Integer.parseInt(part);
                    List<?> list = (List<?>) current;
                    if (index >= 0 && index < list.size()) {
                        current = list.get(index);
                    } else {
                        throw new IllegalArgumentException("Индекс выходит за границы: " + index);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ожидался индекс массива, но получено: " + part);
                }
            } else {
                throw new IllegalArgumentException("Невозможно обработать часть пути: " + part);
            }

            if (current == null) {
                throw new IllegalArgumentException("Путь не найден: " + path);
            }
        }

        return current.toString();
    }
}
