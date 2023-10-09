package org.top.quotagen;

import java.util.Random;

// Примитивная реализация интерфейса
public class PlugGenerator implements IGenerator {

    @Override
    public String getRandomQuota() {
        String[] plugQuotes = {
                "Within these categories, some quotas are global and apply to your",
                "Lisp - is God",
                "Let`s Code!"
        };

        Random r = new Random();
        int randomIndex = r.nextInt(3);     // случайный индекс от 0 до 2
        return plugQuotes[randomIndex];            // цитата по случайному индексу
    }
}
