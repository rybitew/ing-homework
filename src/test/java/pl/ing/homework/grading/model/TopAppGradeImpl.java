package pl.ing.homework.grading.model;

import pl.ing.homework.grading.dto.TopAppGradeDto;

import java.util.Objects;
import java.util.UUID;

public class TopAppGradeImpl implements TopAppGradeDto {
    private String name;
    private UUID appId;

    public TopAppGradeImpl name(String name) {
        this.name = name;
        return this;
    }

    public TopAppGradeImpl appId(UUID uuid) {
        this.appId = uuid;
        return this;
    }

    public String getName() {
        return null;
    }

    public UUID getAppId() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopAppGradeImpl that = (TopAppGradeImpl) o;
        return Objects.equals(name, that.name) && Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, appId);
    }
}
