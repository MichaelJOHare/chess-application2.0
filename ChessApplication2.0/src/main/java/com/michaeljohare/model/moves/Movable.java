package com.michaeljohare.model.moves;

public interface Movable {
    void execute();
    void undo();
    void redo();
}
