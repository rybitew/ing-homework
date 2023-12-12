package pl.ing.homework.grading.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.ing.homework.grading.model.AgeGroup;

@Component
public class AgeGroupConverter implements Converter<String, AgeGroup> {
    @Override
    public AgeGroup convert(String value) {
        return AgeGroup.valueOf(value.toUpperCase());
    }
}
