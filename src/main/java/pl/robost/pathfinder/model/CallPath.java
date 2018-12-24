package pl.robost.pathfinder.model;

import lombok.Data;

import java.util.LinkedHashSet;

@Data
public class CallPath {
    private LinkedHashSet<Method> path = new LinkedHashSet<>();

    CallPath() {
    }

    CallPath(CallPath callPath) {
        this.path = new LinkedHashSet<>(callPath.path);
    }

    boolean add(Method method) {
        return path.add(method);
    }
}
