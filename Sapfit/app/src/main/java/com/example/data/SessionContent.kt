package com.example.data

import com.example.model.Familiarity
import com.example.model.RoadmapAnchor
import com.example.model.SessionAtom

/**
 * Static demo content for the mockup: session atoms and project anchors.
 * We deliberately do not talk to any model — all "AI output" is canned.
 */
object SessionContent {

    fun buildAtomsFor(topic: String): List<SessionAtom> {
        // For the mockup we ship one detailed atom set (Newton). Any topic falls
        // back to it — the wording just re-uses the topic title so the demo feels
        // consistent regardless of what the user typed.
        val t = topic.ifBlank { "Ньютоновская механика" }
        return listOf(
            SessionAtom(
                id = 1,
                title = "Первый закон — инерция",
                explainer = "Тело сохраняет состояние покоя или равномерного движения, пока на него не действует внешняя сила. Проще: если ничего не толкает и не тормозит — движется как ехало.",
                tryQuestion = "Мяч катится по идеально гладкому льду. Что произойдёт со скоростью, если не действовать на него?",
                options = listOf(
                    "Начнёт замедляться и остановится",
                    "Скорость останется неизменной",
                    "Мяч ускорится сам по себе",
                    "Развернётся и покатится назад"
                ),
                correctIndex = 1,
                expertQuestion = "Сформулируй первый закон Ньютона и объясни, что он говорит про инерциальные системы отсчёта."
            ),
            SessionAtom(
                id = 2,
                title = "Второй закон — F = m·a",
                explainer = "Ускорение тела прямо пропорционально силе и обратно — массе. Толкнёшь тележку сильнее — поедет быстрее; поставишь на неё груз — на ту же силу отзовётся слабее.",
                tryQuestion = "На тело массой 2 кг действует сила 10 Н. Какое у него ускорение?",
                options = listOf("2 м/с²", "5 м/с²", "10 м/с²", "20 м/с²"),
                correctIndex = 1,
                expertQuestion = "Что показывает второй закон Ньютона про связь массы, силы и ускорения? В каких единицах измеряется каждая величина?"
            ),
            SessionAtom(
                id = 3,
                title = "Третий закон — действие и противодействие",
                explainer = "Каждой силе есть равная и противоположная. Ты давишь на пол — пол давит на тебя обратно ровно с той же силой. Именно поэтому ты не проваливаешься.",
                tryQuestion = "Ракета выбрасывает газ вниз с силой X. С какой силой газ толкает ракету вверх?",
                options = listOf("Меньше X", "Ровно X", "Больше X", "Ноль — газ ни на что не действует"),
                correctIndex = 1,
                expertQuestion = "Приведи два примера третьего закона Ньютона из повседневной жизни и объясни, где в них действие, а где противодействие."
            ),
            SessionAtom(
                id = 4,
                title = "Итог: как всё связано",
                explainer = "Три закона вместе описывают, как силы меняют движение. Первый — про равновесие, второй — про причину ускорения, третий — про то, что силы всегда парные.",
                tryQuestion = "Какой из законов Ньютона объясняет, почему при резком старте автобуса пассажиров «отбрасывает» назад?",
                options = listOf("Первый", "Второй", "Третий", "Ни один — это про трение"),
                correctIndex = 0,
                expertQuestion = "Своими словами объясни, как три закона Ньютона работают вместе, на примере автомобиля, который трогается с места."
            )
        )
    }

    fun buildAnchorsFor(title: String, subject: String): List<RoadmapAnchor> {
        // Small deterministic roadmap for the mockup — 6 anchors.
        val topicBase = title.trim().ifBlank { subject }
        val stems = listOf(
            "Введение и основные понятия",
            "Ключевые определения",
            "Главные формулы и правила",
            "Разбор типовых задач",
            "Частые ошибки и подводные камни",
            "Итоговая проверка и закрепление"
        )
        return stems.mapIndexed { i, stem ->
            RoadmapAnchor(
                id = "a_${i}_${System.currentTimeMillis()}",
                title = "$stem — $topicBase",
                familiarity = Familiarity.NOVICE
            )
        }
    }
}
