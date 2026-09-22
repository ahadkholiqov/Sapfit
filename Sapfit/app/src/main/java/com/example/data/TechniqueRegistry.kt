package com.example.data

data class TechniqueEntry(
    val id: String,
    val nameRu: String,
    val nameEn: String,
    val shortRu: String,
    val shortEn: String,
    val isDetailed: Boolean,
    val descRu: String? = null,
    val descEn: String? = null,
    val rationaleRu: String? = null,
    val rationaleEn: String? = null,
    val exampleRu: String? = null,
    val exampleEn: String? = null,
    val taskRu: String? = null,
    val taskEn: String? = null,
    val tagsRu: List<String> = emptyList(),
    val tagsEn: List<String> = emptyList()
) {
    fun name(lang: AppLanguage) = if (lang == AppLanguage.EN) nameEn else nameRu
    fun short(lang: AppLanguage) = if (lang == AppLanguage.EN) shortEn else shortRu
    fun desc(lang: AppLanguage) = if (lang == AppLanguage.EN) descEn else descRu
    fun rationale(lang: AppLanguage) = if (lang == AppLanguage.EN) rationaleEn else rationaleRu
    fun example(lang: AppLanguage) = if (lang == AppLanguage.EN) exampleEn else exampleRu
    fun task(lang: AppLanguage) = if (lang == AppLanguage.EN) taskEn else taskRu
    fun tags(lang: AppLanguage) = if (lang == AppLanguage.EN) tagsEn else tagsRu
}

object TechniqueRegistry {
    val all: List<TechniqueEntry> = listOf(
        TechniqueEntry(
            id = "feynman",
            nameRu = "Метод Фейнмана",
            nameEn = "Feynman Technique",
            shortRu = "Объясни простыми словами — где путаешься, там пробел.",
            shortEn = "Explain in plain words — where you stumble is the gap.",
            isDetailed = true,
            descRu = "Объясни тему простыми словами, как будто рассказываешь младшему брату. Где начинаешь путаться или уходишь в сложные термины — там пробел в понимании, к нему возвращаемся.",
            descEn = "Explain the topic in simple words, as if telling a younger sibling. Wherever you start stumbling or slipping into jargon — that's the gap in understanding to go back to.",
            rationaleRu = "Комбинация активного припоминания, разбиения на смысловые блоки и метакогнитивного контроля — устойчиво предсказывает долгосрочное удержание материала.",
            rationaleEn = "A combination of active recall, chunking, and metacognitive control — a robust predictor of long-term retention.",
            exampleRu = "Вместо «митохондрия — органелла, осуществляющая клеточное дыхание» — «это как маленькая электростанция внутри клетки, которая берёт еду и кислород и превращает их в энергию».",
            exampleEn = "Instead of \"the mitochondrion is an organelle carrying out cellular respiration\" — \"it's like a tiny power plant inside the cell that takes food and oxygen and turns them into energy.\"",
            taskRu = "Объясни своими словами, зачем растению нужен фотосинтез — представь, что говоришь это десятилетнему.",
            taskEn = "In your own words, explain why a plant needs photosynthesis — imagine you're talking to a ten-year-old.",
            tagsRu = listOf("биология", "история", "обществознание", "сложные концепции"),
            tagsEn = listOf("biology", "history", "social studies", "complex concepts")
        ),
        TechniqueEntry(
            id = "dual_coding",
            nameRu = "Двойное кодирование",
            nameEn = "Dual Coding",
            shortRu = "Держим текст и схему рядом — два канала памяти сильнее одного.",
            shortEn = "Keep text and a visual together — two memory channels beat one.",
            isDetailed = true,
            descRu = "Держим текст и визуальную схему вместе, не по отдельности. Мозг кодирует их в разных каналах памяти, и вместе они держатся крепче, чем любой из них поодиночке.",
            descEn = "Keep the text and a visual schema together, not separately. The brain encodes them through different memory channels, and together they stick better than either alone.",
            rationaleRu = "Теория Пайвио и принципы мультимедийного обучения Мэйера — подтверждено многократно на разных предметах.",
            rationaleEn = "Paivio's dual-coding theory and Mayer's multimedia learning principles — repeatedly confirmed across subjects.",
            exampleRu = "Рядом с определением «круговорот воды» — простая схема со стрелками: испарение → облака → осадки → сток.",
            exampleEn = "Next to the \"water cycle\" definition — a simple arrow diagram: evaporation → clouds → precipitation → runoff.",
            taskRu = "Посмотри на схему клеточного деления и подпиши, что происходит на каждой стадии, не подглядывая в текст.",
            taskEn = "Look at the cell-division diagram and label what happens at each stage without peeking at the text.",
            tagsRu = listOf("биология", "физика", "география", "материал с визуальной структурой"),
            tagsEn = listOf("biology", "physics", "geography", "visually structured material")
        ),
        TechniqueEntry(
            id = "analogies",
            nameRu = "Аналогии",
            nameEn = "Analogies",
            shortRu = "Объясняем новое через структурное сходство со знакомым.",
            shortEn = "Explain the new through structural similarity to the familiar.",
            isDetailed = true,
            descRu = "Объясняем новое через структурное сходство с уже знакомым. Электрический ток — как поток воды в трубе, только вместо давления — напряжение.",
            descEn = "Explain the new through structural similarity to something already known. Electric current is like water flow in a pipe, with voltage in place of pressure.",
            rationaleRu = "Structure-mapping theory Джентнер — ускоряет построение ментальной модели новой темы за счёт готовой модели знакомой.",
            rationaleEn = "Gentner's structure-mapping theory — speeds up building the mental model of a new topic by reusing the model of a familiar one.",
            exampleRu = "Клеточная мембрана — как таможня на границе: пропускает нужное, задерживает лишнее.",
            exampleEn = "The cell membrane is like customs at a border: it lets through what's needed and holds back the rest.",
            taskRu = "Придумай свою аналогию для того, как работает иммунная система, используя что-то из повседневной жизни.",
            taskEn = "Come up with your own analogy for how the immune system works, using something from everyday life.",
            tagsRu = listOf("физика", "химия", "абстрактные понятия", "новые темы без хорошего примера в учебнике"),
            tagsEn = listOf("physics", "chemistry", "abstract concepts", "new topics without a good textbook example")
        ),
        TechniqueEntry(
            id = "elaborative_interrogation",
            nameRu = "Разработочный опрос",
            nameEn = "Elaborative Interrogation",
            shortRu = "Задаём себе «почему это так» — работает на причинно-следственных фактах.",
            shortEn = "Ask yourself \"why is this so\" — best for cause-and-effect facts.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "self_explanation",
            nameRu = "Самообъяснение",
            nameEn = "Self-Explanation",
            shortRu = "Проговариваем своими словами каждый шаг решения — для процедур и разбора задач.",
            shortEn = "Say each step of the solution in your own words — for procedures and worked problems.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "concrete_examples",
            nameRu = "Конкретные примеры",
            nameEn = "Concrete Examples",
            shortRu = "Иллюстрируем абстрактное понятие через два-три реальных случая.",
            shortEn = "Illustrate an abstract concept with two or three real cases.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "concept_maps",
            nameRu = "Концептуальные карты",
            nameEn = "Concept Maps",
            shortRu = "Строим схему связей между понятиями по памяти, без опоры на источник.",
            shortEn = "Draw a map of relationships between concepts from memory, without the source.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "worked_examples",
            nameRu = "Разбор готовых решений",
            nameEn = "Worked Examples",
            shortRu = "Сначала изучаем полностью разобранный пример, потом решаем похожую задачу сами.",
            shortEn = "Study a fully worked example first, then solve a similar problem on your own.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "chunking",
            nameRu = "Чанкинг",
            nameEn = "Chunking",
            shortRu = "Разбиваем длинные списки на смысловые группы по 3–4 элемента.",
            shortEn = "Break long lists into meaningful groups of 3–4 items.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "mnemonics",
            nameRu = "Мнемоники",
            nameEn = "Mnemonics",
            shortRu = "Метод локусов, ключевые слова, рифмы — для произвольной информации вроде дат.",
            shortEn = "Method of loci, keywords, rhymes — for arbitrary info like dates or vocab.",
            isDetailed = false
        ),
        TechniqueEntry(
            id = "pretesting",
            nameRu = "Генеративный эффект / pretesting",
            nameEn = "Generative Effect / Pretesting",
            shortRu = "Пробуем ответить на вопрос до объяснения материала, даже наугад.",
            shortEn = "Try to answer the question before the explanation — even guessing helps.",
            isDetailed = false
        )
    )

    fun byId(id: String): TechniqueEntry? = all.find { it.id == id }
}
