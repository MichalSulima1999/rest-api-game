package com.michael1099.rest_rpg.statistics.observer;

import com.michael1099.rest_rpg.character.model.Character;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

// Tydzień 6 - Observer
// Ta klasa jest rozszerzona przez ApplicationEvent. Eventy posiadają informacje potrzebne obserwatorowi
// W klasie EndFight znajduje się ApplicationEventPublisher, który pozwala na wyzwalanie eventów
// Event jest subskrybowany w EmailService. Po wywołaniu eventu, ta klasa obsługuje go dzięki @EventListener.
// W tym wypadku obsługiwana jest wysyłka maila po osiągnięciu poziomu przez postać.
@Getter
public class LevelUpEvent extends ApplicationEvent {

    private final Character character;

    public LevelUpEvent(Object source, Character character) {
        super(source);
        this.character = character;
    }
}
// Koniec Tydzień 6 - Observer
