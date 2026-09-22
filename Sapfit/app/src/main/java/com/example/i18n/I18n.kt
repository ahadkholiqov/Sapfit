package com.example.i18n

import com.example.data.AppLanguage

object Str {
    fun get(lang: AppLanguage, ru: String, en: String): String {
        return if (lang == AppLanguage.EN) en else ru
    }

    // Tabs
    fun tabHome(lang: AppLanguage) = get(lang, "Главная", "Home")
    fun tabProjects(lang: AppLanguage) = get(lang, "Проекты", "Projects")
    fun tabAbout(lang: AppLanguage) = get(lang, "О тебе", "Insights")
    fun tabProfile(lang: AppLanguage) = get(lang, "Профиль", "Profile")

    // Home Screen — new mode buttons per spec §5
    fun modeQuick(lang: AppLanguage) = get(lang, "Быстрая сессия", "Quick session")
    fun modeProject(lang: AppLanguage) = get(lang, "Проект", "Project")

    // Home phrase carousel (spec §5)
    fun homePhrases(lang: AppLanguage): List<String> = if (lang == AppLanguage.EN) listOf(
        "What shall we work on today?",
        "Pick up the project?",
        "Start a new session?",
        "Where do we begin?"
    ) else listOf(
        "Чем займёмся сегодня?",
        "Продолжим проект?",
        "Начнём новую сессию?",
        "С чего начнём?"
    )

    // Gemini-style input
    fun homeInputPlaceholder(lang: AppLanguage) =
        get(lang, "Вставь текст или опиши тему…", "Paste text or describe a topic…")
    fun attachCamera(lang: AppLanguage) = get(lang, "Снять фото", "Take a photo")
    fun attachGallery(lang: AppLanguage) = get(lang, "Фото из галереи", "Photo from gallery")
    fun attachFile(lang: AppLanguage) = get(lang, "Файл", "File")

    // Explainer banners (spec §13)
    fun explainerProjectsTitle(lang: AppLanguage) =
        get(lang, "Раздел «Проекты»", "Projects section")
    fun explainerProjectsText(lang: AppLanguage) = get(
        lang,
        "Здесь живут активные долгие сессии и история коротких. Закреплённые — всегда сверху.",
        "Long-running sessions and short-session history live here. Pinned ones always stay on top."
    )
    fun explainerAboutTitle(lang: AppLanguage) =
        get(lang, "Раздел «О тебе»", "Insights section")
    fun explainerAboutText(lang: AppLanguage) = get(
        lang,
        "Статистика, приоритеты техник и наблюдения системы про твой стиль обучения.",
        "Stats, technique priorities, and the app's observations about your study style."
    )
    fun explainerProfileTitle(lang: AppLanguage) =
        get(lang, "Раздел «Профиль»", "Profile section")
    fun explainerProfileText(lang: AppLanguage) = get(
        lang,
        "Общие настройки продукта: аккаунт, занятия, приватность, помощь.",
        "General app settings: account, study schedule, privacy, help."
    )

    // ==================== Onboarding wave 1 (spec §2) ====================
    fun onbSkip(lang: AppLanguage) = get(lang, "Пропустить (демо)", "Skip (demo)")
    fun onbNext(lang: AppLanguage) = get(lang, "Далее", "Next")
    fun onbBack(lang: AppLanguage) = get(lang, "Назад", "Back")

    // Screen 1 — welcome
    fun obWelcomeGreeting(lang: AppLanguage) = get(
        lang,
        "Sapfit — учит тебя учиться, а не даёт готовые ответы.",
        "Sapfit teaches you how to learn — not just hands you answers."
    )
    fun obSignInGoogle(lang: AppLanguage) = get(lang, "Войти через Google", "Continue with Google")
    fun obSignInApple(lang: AppLanguage) = get(lang, "Войти через iCloud", "Continue with iCloud")
    fun obAgreeTerms(lang: AppLanguage) =
        get(lang, "Принимаю условия использования", "I accept the Terms of Use")
    fun obAgreePrivacy(lang: AppLanguage) =
        get(lang, "Принимаю политику конфиденциальности", "I accept the Privacy Policy")

    // Screen 2 — profile
    fun obProfileTitle(lang: AppLanguage) = get(lang, "Расскажи немного о себе", "Tell us a bit about you")
    fun obProfileSub(lang: AppLanguage) = get(
        lang,
        "Ответь на 10 вопросов, чтобы мы собрали первую гипотезу о том, как тебе лучше учиться.",
        "Answer 10 quick questions so we can put together a first hypothesis on how you learn best."
    )
    fun obNameLabel(lang: AppLanguage) = get(lang, "Имя", "Name")
    fun obNameHint(lang: AppLanguage) = get(lang, "Как тебя зовут", "What's your name")
    fun obAgeLabel(lang: AppLanguage) = get(lang, "Возраст", "Age")
    fun obAgeHint(lang: AppLanguage) = get(lang, "Полных лет", "Your age")
    fun obSituationLabel(lang: AppLanguage) = get(lang, "Учебная ситуация", "Learning situation")
    fun obSituationSchool(lang: AppLanguage) = get(lang, "Школьник", "School student")
    fun obSituationExam(lang: AppLanguage) = get(lang, "Готовлюсь к экзаменам", "Prepping for exams")
    fun obSituationUni(lang: AppLanguage) = get(lang, "Студент", "University student")
    fun obSituationSelf(lang: AppLanguage) = get(lang, "Учу для себя", "Studying for myself")
    fun obStartSetup(lang: AppLanguage) = get(lang, "Пройти первую настройку", "Start first setup")

    // Screens 3–12 — 10 questions
    fun obQ1Prompt(lang: AppLanguage) = get(lang, "Сколько часов ты в среднем спишь в будни?", "How many hours do you sleep on weekdays on average?")
    fun obQ1Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Less than 5", "5–6", "7–8", "More than 8")
    else listOf("Меньше 5", "5–6", "7–8", "Больше 8")

    fun obQ2Prompt(lang: AppLanguage) = get(lang, "Сколько времени в день у тебя уходит на соцсети и развлекательные приложения?", "How much time per day do you spend on social media and entertainment apps?")
    fun obQ2Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Less than an hour", "1–3 hours", "3–5 hours", "More than 5 hours")
    else listOf("Меньше часа", "1–3 часа", "3–5 часов", "Больше 5 часов")

    fun obQ3Prompt(lang: AppLanguage) = get(lang, "Как часто ты теряешь фокус во время занятий?", "How often do you lose focus while studying?")
    fun obQ3Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Almost never", "Sometimes", "Often", "Almost always")
    else listOf("Почти никогда", "Иногда", "Часто", "Почти всегда")

    fun obQ4Prompt(lang: AppLanguage) = get(lang, "Насколько тяжело тебе начать заниматься, когда ты сел за уроки?", "How hard is it to actually start studying once you sit down?")
    fun obQ4Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Start easily right away", "Slight resistance", "Usually slow to get going", "Almost always put it off")
    else listOf("Легко начинаю сразу", "Есть небольшое сопротивление", "Обычно долго раскачиваюсь", "Почти всегда откладываю")

    fun obQ5Prompt(lang: AppLanguage) = get(lang, "Как ты обычно готовишься к контрольной?", "How do you usually prepare for a test?")
    fun obQ5Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Re-read notes", "Rewrite the material", "Do problems and examples", "Re-explain it to someone or myself")
    else listOf("Перечитываю конспект", "Переписываю материал", "Решаю задачи и примеры", "Пересказываю кому-то или себе вслух")

    fun obQ6Prompt(lang: AppLanguage) = get(lang, "Насколько чётко ты понимаешь, зачем именно сейчас учишь то, что учишь?", "How clearly do you understand why you're studying what you're studying?")
    fun obQ6Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Very clearly", "Roughly understand", "Rather don't", "I don't — just have to")
    else listOf("Очень чётко", "Скорее понимаю", "Скорее не понимаю", "Не понимаю, просто надо")

    fun obQ7Prompt(lang: AppLanguage) = get(lang, "Бывает, что ты учил-учил, а на контрольной не смог вспомнить?", "Ever studied hard and then couldn't recall it during the test?")
    fun obQ7Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Almost never", "Sometimes", "Often", "Almost always")
    else listOf("Почти никогда", "Иногда", "Часто", "Почти всегда")

    fun obQ8Prompt(lang: AppLanguage) = get(lang, "Сколько часов вчера ты потратил на учёбу вне школьных занятий?", "How many hours did you spend studying yesterday outside of school?")
    fun obQ8Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Less than an hour", "1–2 hours", "2–4 hours", "More than 4 hours")
    else listOf("Меньше часа", "1–2 часа", "2–4 часа", "Больше 4 часов")

    fun obQ9Prompt(lang: AppLanguage) = get(lang, "Как в целом ты себя чувствуешь в последнее время, когда думаешь об учёбе?", "How do you feel these days when you think about studying?")
    fun obQ9Options(lang: AppLanguage) = if (lang == AppLanguage.EN)
        listOf("Calm", "A bit tense", "Anxious", "Really hard")
    else listOf("Спокойно", "Немного напряжённо", "Тревожно", "Очень тяжело")

    fun obQ10Prompt(lang: AppLanguage) = get(lang, "Если в двух словах — что тебе сейчас мешает учиться больше всего?", "In a couple of words — what's getting in the way of studying most right now?")
    fun obQ10Placeholder(lang: AppLanguage) = get(lang, "Можно пропустить", "You can skip this")

    // Screen 13 — processing
    fun obProcessing1(lang: AppLanguage) = get(lang, "Обрабатываю ответы…", "Processing your answers…")
    fun obProcessing2(lang: AppLanguage) = get(lang, "Строю первую гипотезу…", "Building the first hypothesis…")
    fun obProcessing3(lang: AppLanguage) = get(lang, "Готовлю наблюдения…", "Preparing observations…")

    // Screen 14 — observations
    fun obObservationsTitle(lang: AppLanguage) = get(lang, "Что я заметил", "What I've noticed")
    fun obObservation1(lang: AppLanguage) = get(
        lang,
        "Ты спишь около 5–6 часов в будни. Это ниже уровня, при котором мозг подростка нормально закрепляет то, что учит — во сне идёт консолидация памяти. Мы будем короче делать вечерние сессии и переносить больше повторений на утро.",
        "You sleep around 5–6 hours on weekdays. That's below the level at which a teenager's brain consolidates what it learns — memory consolidation happens during sleep. We'll keep evening sessions shorter and shift more review to the mornings."
    )
    fun obObservation2(lang: AppLanguage) = get(
        lang,
        "Ты сказал, что обычно перечитываешь конспект перед контрольной. Это одна из самых субъективно комфортных, но объективно слабых стратегий — она даёт ощущение понимания без реального запоминания. Мы сразу переведём тебя на активное припоминание вместо перечитывания.",
        "You said you usually re-read your notes before a test. That's one of the most subjectively comfortable but objectively weak strategies — it feels like understanding without actual retention. We'll switch you to active recall instead of re-reading right away."
    )
    fun obObservation3(lang: AppLanguage) = get(
        lang,
        "Бывает, что учил-учил, а на контрольной не вспомнил — и это довольно частое чувство. Обычно это не про способности, а про то, что материал не был закреплён вовремя. Мы будем возвращать тебя к темам заранее, до того как они успеют забыться.",
        "Sometimes you study hard and still blank on the test — that's a common feeling. It's usually not about ability; it's that the material wasn't consolidated in time. We'll bring topics back to you before they've had a chance to fade."
    )
    fun obContinue(lang: AppLanguage) = get(lang, "Продолжить", "Continue")

    // Screens 15–17 — mini-session
    fun obMiniIntroTitle(lang: AppLanguage) = get(lang, "Попробуем прямо сейчас", "Let's try it right now")
    fun obMiniIntroSub(lang: AppLanguage) = get(
        lang,
        "Быстрая мини-сессия на минуту-две — чтобы ты почувствовал, как продукт работает в деле.",
        "A quick minute-long session so you can feel how the product actually works."
    )
    fun obMiniStart(lang: AppLanguage) = get(lang, "Начать", "Start")
    fun obMiniQuestion(lang: AppLanguage) = get(
        lang,
        "Быстро своими словами: зачем растению нужен фотосинтез? Ответь так, будто объясняешь десятилетнему.",
        "Quickly, in your own words: why does a plant need photosynthesis? Explain it as if to a ten-year-old."
    )
    fun obMiniPlaceholder(lang: AppLanguage) = get(lang, "Твой ответ…", "Your answer…")
    fun obMiniDoneTitle(lang: AppLanguage) = get(lang, "Отлично, это и есть Sapfit", "That's Sapfit in a nutshell")
    fun obMiniDoneSub(lang: AppLanguage) = get(
        lang,
        "Ты объяснил, а не просто прочитал. Именно так и запоминается по-настоящему.",
        "You explained it rather than just reading it. That's how things actually stick."
    )
    fun obDoneTitle(lang: AppLanguage) = get(lang, "Готово", "You're set")
    fun obDoneSub(lang: AppLanguage) = get(
        lang,
        "Стартовые настройки собраны. Ты всегда сможешь их поменять в разделе «О тебе».",
        "The starting settings are ready. You can change them any time under Insights."
    )
    fun obDoneCta(lang: AppLanguage) = get(lang, "На главную", "Go to Home")

    // ==================== Session flow (spec §6) ====================
    fun sessProcessing1(lang: AppLanguage) = get(lang, "Анализируем материалы…", "Reading through the material…")
    fun sessProcessing2(lang: AppLanguage) = get(lang, "Собираем информацию…", "Pulling things together…")
    fun sessProcessing3(lang: AppLanguage) = get(lang, "Подбираем подход…", "Picking an approach…")

    fun branchTitle(lang: AppLanguage) = get(lang, "Насколько ты знаком с темой?", "How familiar are you with the topic?")
    fun branchNoviceTitle(lang: AppLanguage) = get(lang, "Не знаком, хочу изучить", "Not familiar — I want to learn it")
    fun branchNoviceSub(lang: AppLanguage) = get(lang, "Разберём с нуля, с объяснениями", "We'll go from scratch, with explanations")
    fun branchPartialTitle(lang: AppLanguage) = get(lang, "Немного знаю, хочу углубиться", "I know a bit — I want to go deeper")
    fun branchPartialSub(lang: AppLanguage) = get(lang, "Смешаем вопросы и объяснения", "A mix of questions and explanations")
    fun branchExpertTitle(lang: AppLanguage) = get(lang, "Знаком, хочу повторить", "I know it — I just want to review")
    fun branchExpertSub(lang: AppLanguage) = get(lang, "Прямые вопросы без подсказок", "Direct questions, no hints")

    fun sessDoneTitle(lang: AppLanguage) = get(lang, "Сессия завершена", "Session complete")
    fun sessDoneSub(lang: AppLanguage) = get(
        lang,
        "Сохранил в историю. Можно открыть снова, добавить материалы или превратить в проект.",
        "Saved to history. You can reopen it, add materials, or turn it into a project."
    )
    fun sessToHome(lang: AppLanguage) = get(lang, "На главную", "Back to Home")
    fun sessContinueBtn(lang: AppLanguage) = get(lang, "Дальше", "Next")
    fun sessShowExplanation(lang: AppLanguage) = get(lang, "Показать объяснение", "Show explanation")

    // ==================== Project creation (spec §7) ====================
    fun pcTitle(lang: AppLanguage) = get(lang, "Новый проект", "New project")
    fun pcNameLabel(lang: AppLanguage) = get(lang, "Название проекта", "Project title")
    fun pcNameHint(lang: AppLanguage) = get(lang, "Например: ОГЭ по физике", "e.g. Physics exam prep")
    fun pcGoalLabel(lang: AppLanguage) = get(lang, "Цель", "Goal")
    fun pcGoalTest(lang: AppLanguage) = get(lang, "Контрольная", "Test")
    fun pcGoalExam(lang: AppLanguage) = get(lang, "Экзамен", "Exam")
    fun pcGoalSelf(lang: AppLanguage) = get(lang, "Освоение материала", "Just learn the material")
    fun pcDeadlineLabel(lang: AppLanguage) = get(lang, "Дедлайн (опционально)", "Deadline (optional)")
    fun pcDeadlineHint(lang: AppLanguage) = get(lang, "например 14.08", "e.g. Aug 14")
    fun pcCreateBtn(lang: AppLanguage) = get(lang, "Создать проект", "Create project")
    fun pcCancelBtn(lang: AppLanguage) = get(lang, "Отмена", "Cancel")

    fun pcProcessing1(lang: AppLanguage) = get(lang, "Читаю материалы…", "Reading the materials…")
    fun pcProcessing2(lang: AppLanguage) = get(lang, "Строю дорожную карту…", "Building the roadmap…")
    fun pcProcessing3(lang: AppLanguage) = get(lang, "Рассчитываю расписание…", "Working out the schedule…")

    fun pcCalibTitle(lang: AppLanguage) = get(lang, "Быстрая калибровка", "Quick calibration")
    fun pcCalibSub(lang: AppLanguage) = get(
        lang,
        "Отметь для каждой темы, насколько ты уже с ней знаком — по этому подберётся режим работы.",
        "Mark for each topic how familiar you already are — this picks the working mode."
    )
    fun famNovice(lang: AppLanguage) = get(lang, "Не знаком", "New to it")
    fun famPartial(lang: AppLanguage) = get(lang, "Немного знаю", "A bit familiar")
    fun famExpert(lang: AppLanguage) = get(lang, "Повторить", "Just review")
    fun pcCalibDone(lang: AppLanguage) = get(lang, "Готово, к проекту", "Done — open project")

    // ==================== Project detail (spec §9) ====================
    fun pdProgressLabel(lang: AppLanguage) = get(lang, "Прогресс", "Progress")
    fun pdLocked(lang: AppLanguage) = get(lang, "Закреплено", "Locked in")
    fun pdLearning(lang: AppLanguage) = get(lang, "Изучаем", "Learning")
    fun pdAhead(lang: AppLanguage) = get(lang, "Впереди", "Ahead")
    fun pdMaterials(lang: AppLanguage) = get(lang, "Материалы", "Materials")
    fun pdAddMaterial(lang: AppLanguage) = get(lang, "+ Добавить материал", "+ Add material")
    fun pdSchedule(lang: AppLanguage) = get(lang, "Расписание сессий", "Session schedule")
    fun pdFormatTitle(lang: AppLanguage) = get(lang, "Формат работы для этого проекта", "Working format for this project")
    fun pdFormatLength(lang: AppLanguage) = get(lang, "Длина сессии", "Session length")
    fun pdFormatTime(lang: AppLanguage) = get(lang, "Время суток", "Time of day")
    fun pdFormatInterleave(lang: AppLanguage) = get(lang, "Чередование тем", "Topic interleaving")
    fun pdTimeMorning(lang: AppLanguage) = get(lang, "Утро", "Morning")
    fun pdTimeDay(lang: AppLanguage) = get(lang, "День", "Day")
    fun pdTimeEvening(lang: AppLanguage) = get(lang, "Вечер", "Evening")
    fun pdIntSoft(lang: AppLanguage) = get(lang, "Мягко", "Gentle")
    fun pdIntStandard(lang: AppLanguage) = get(lang, "Стандартно", "Standard")
    fun pdIntAggressive(lang: AppLanguage) = get(lang, "Агрессивно", "Aggressive")

    // ==================== Wave-2 onboarding (spec §3) ====================
    fun w2BannerTitle(lang: AppLanguage) =
        get(lang, "Хочешь настроить точнее?", "Want to fine-tune it?")
    fun w2BannerText(lang: AppLanguage) = get(
        lang,
        "Ещё 15–20 минут — и подберём техники и формат точнее под тебя.",
        "Another 15–20 minutes and we'll match techniques and format to you more precisely."
    )
    fun w2BannerRun(lang: AppLanguage) = get(lang, "Пройти настройку", "Fine-tune")
    fun w2BannerCancel(lang: AppLanguage) = get(lang, "Позже", "Later")

    fun w2IntroTitle(lang: AppLanguage) = get(lang, "Настроим точнее", "Let's fine-tune")
    fun w2IntroSub(lang: AppLanguage) = get(
        lang,
        "Попробуем несколько способов работы с материалом на практике — займёт 15–20 минут.",
        "We'll try a few ways of working with the material — takes 15–20 minutes."
    )
    fun w2Start(lang: AppLanguage) = get(lang, "Пройти сейчас", "Start now")
    fun w2Cancel(lang: AppLanguage) = get(lang, "Отмена", "Cancel")

    fun w2SubjectTitle(lang: AppLanguage) = get(lang, "В чём хочешь стать сильнее?", "What do you want to get stronger at?")
    fun w2Subjects(lang: AppLanguage): List<String> = if (lang == AppLanguage.EN) listOf(
        "Biology", "History", "Math", "Physics", "English", "Social studies", "Other"
    ) else listOf(
        "Биология", "История", "Математика", "Физика", "Английский", "Обществознание", "Другое"
    )

    fun w2DemoLead(lang: AppLanguage) = get(lang, "Возьмём демо-тему:", "Let's use a demo topic:")
    fun w2DemoSub(lang: AppLanguage) = get(
        lang,
        "Дальше — три техники обучения, попробуем каждую на этой теме по паре минут. В конце скажешь, что подошло.",
        "Next: three learning techniques — we'll try each one on this topic for a couple of minutes. At the end you tell us which fit."
    )
    fun w2DemoStart(lang: AppLanguage) = get(lang, "Начать", "Start")
    fun w2DemoTopicFor(lang: AppLanguage, subjectKey: String): String {
        val ru = when (subjectKey) {
            "Биология" -> "клеточное строение"
            "История" -> "Первая мировая война"
            "Математика" -> "квадратные уравнения"
            "Физика" -> "законы Ньютона"
            "Английский" -> "система времён Present"
            "Обществознание" -> "формы государства"
            else -> "выбранная тема"
        }
        val en = when (subjectKey) {
            "Biology" -> "cell structure"
            "History" -> "World War I"
            "Math" -> "quadratic equations"
            "Physics" -> "Newton's laws"
            "English" -> "the Present tense system"
            "Social studies" -> "forms of government"
            else -> "your chosen topic"
        }
        return if (lang == AppLanguage.EN) en else ru
    }

    fun w2TechIntro(lang: AppLanguage) = get(lang, "Пробуем технику", "Trying the technique")
    fun w2TechRating(lang: AppLanguage) = get(lang, "Как тебе, зашло?", "How did it feel?")
    fun w2RateLoved(lang: AppLanguage) = get(lang, "Зашло", "Loved it")
    fun w2RateOk(lang: AppLanguage) = get(lang, "Скорее да", "Kind of")
    fun w2RateMeh(lang: AppLanguage) = get(lang, "Скорее нет", "Not really")
    fun w2RateNo(lang: AppLanguage) = get(lang, "Не моё", "Not for me")

    fun w2FormatTitle(lang: AppLanguage) = get(lang, "Пара вопросов про формат", "A couple of format questions")
    fun w2FormatQ1(lang: AppLanguage) = get(lang, "Когда тебе продуктивнее?", "When are you more productive?")
    fun w2FormatMorning(lang: AppLanguage) = get(lang, "Утром", "Morning")
    fun w2FormatEvening(lang: AppLanguage) = get(lang, "Вечером", "Evening")
    fun w2FormatQ2(lang: AppLanguage) = get(lang, "Какой формат подходов удобнее?", "Which session shape suits you better?")
    fun w2FormatShort(lang: AppLanguage) = get(lang, "Короткими интенсивами", "Short intense bursts")
    fun w2FormatLong(lang: AppLanguage) = get(lang, "Длинными погружениями", "Long deep dives")
    fun w2FormatQ3(lang: AppLanguage) = get(lang, "Как удобнее с темами?", "How do you prefer topics?")
    fun w2FormatMix(lang: AppLanguage) = get(lang, "Перемешиваются", "Interleaved")
    fun w2FormatSingle(lang: AppLanguage) = get(lang, "Одна за раз", "One at a time")

    fun w2ConfigTitle(lang: AppLanguage) = get(lang, "Вот стартовая гипотеза", "Here's the starting hypothesis")
    fun w2ConfigSub(lang: AppLanguage) = get(
        lang,
        "Можешь поменять что угодно уже сейчас — или оставить как есть.",
        "You can change anything now, or leave it as it is."
    )
    fun w2ConfigTechniques(lang: AppLanguage) = get(lang, "Приоритеты техник", "Technique priorities")
    fun w2ConfigFormat(lang: AppLanguage) = get(lang, "Формат работы", "Working format")
    fun w2Finish(lang: AppLanguage) = get(lang, "Оставить как есть", "Keep it")
    fun w2EditLater(lang: AppLanguage) = get(lang, "Отредактирую позже", "I'll edit later")

    // ==================== Profile sub-screens (spec §12) ====================
    fun psAccountEmailLabel(lang: AppLanguage) = get(lang, "Email", "Email")
    fun psAccountEmailValue(lang: AppLanguage) = get(lang, "demo@sapfit.app", "demo@sapfit.app")
    fun psAccountSubLabel(lang: AppLanguage) = get(lang, "Подписка", "Subscription")
    fun psAccountSubValue(lang: AppLanguage) = get(lang, "Свободный доступ (демо)", "Free access (demo)")
    fun psAccountLinksLabel(lang: AppLanguage) = get(lang, "Привязки", "Linked accounts")
    fun psAccountLinksValue(lang: AppLanguage) = get(lang, "Google · Apple", "Google · Apple")
    fun psAccountDelete(lang: AppLanguage) = get(lang, "Удалить аккаунт", "Delete account")

    fun psLessonsLength(lang: AppLanguage) = get(lang, "Длина сессии по умолчанию", "Default session length")
    fun psLessonsLengthValue(lang: AppLanguage) = get(lang, "30 мин", "30 min")
    fun psLessonsTime(lang: AppLanguage) = get(lang, "Время суток по умолчанию", "Default time of day")
    fun psLessonsTimeValue(lang: AppLanguage) = get(lang, "Вечер", "Evening")
    fun psLessonsRest(lang: AppLanguage) = get(lang, "Дни без работы", "No-study days")
    fun psLessonsRestValue(lang: AppLanguage) = get(lang, "Воскресенье", "Sunday")
    fun psLessonsHint(lang: AppLanguage) = get(
        lang,
        "Заглушка настроек: в реальном приложении здесь будут работающие переключатели.",
        "Placeholder settings — real toggles ship in the full app."
    )

    fun psFocusIntro(lang: AppLanguage) = get(
        lang,
        "Пока идёт сессия, эти приложения и уведомления блокируются. Интегрируется с iOS Screen Time / Android Digital Wellbeing.",
        "During a session, these apps and notifications are blocked. Integrates with iOS Screen Time / Android Digital Wellbeing."
    )
    fun psFocusBlock(lang: AppLanguage) = get(lang, "блок", "block")
    fun psFocusNotifOnly(lang: AppLanguage) = get(lang, "только уведомления", "notifications only")
    fun psFocusHint(lang: AppLanguage) = get(
        lang,
        "Заглушка: настоящая интеграция с системными API — в приложении, не в мокапе.",
        "Placeholder — real system-API integration ships in the full app."
    )

    fun psPrivacyUseTraining(lang: AppLanguage) = get(lang, "Использовать ответы для обучения моделей", "Use answers to improve models")
    fun psPrivacyOn(lang: AppLanguage) = get(lang, "включено", "on")
    fun psPrivacyOff(lang: AppLanguage) = get(lang, "выключено", "off")
    fun psPrivacyExport(lang: AppLanguage) = get(lang, "Экспортировать мои данные", "Export my data")

    fun psAppearanceTheme(lang: AppLanguage) = get(lang, "Тема", "Theme")
    fun psAppearanceThemeDark(lang: AppLanguage) = get(lang, "Тёмная", "Dark")
    fun psAppearanceThemeLight(lang: AppLanguage) = get(lang, "Светлая", "Light")
    fun psAppearanceFont(lang: AppLanguage) = get(lang, "Размер шрифта", "Font size")
    fun psAppearanceFontValue(lang: AppLanguage) = get(lang, "Стандартный", "Standard")
    fun psAppearanceLang(lang: AppLanguage) = get(lang, "Язык интерфейса", "Interface language")
    fun psAppearanceLangValueRu(lang: AppLanguage) = get(lang, "Русский", "Russian")
    fun psAppearanceLangValueEn(lang: AppLanguage) = get(lang, "Английский", "English")

    fun psHelpScienceTitle(lang: AppLanguage) = get(lang, "Как устроена наука обучения", "How the science of learning works")
    fun psHelpScienceSub(lang: AppLanguage) = get(
        lang,
        "Короткие тексты про активное припоминание, спейсинг, интерливинг — что доказано, а что нет.",
        "Short reads on active recall, spacing, interleaving — what's evidence-backed and what isn't."
    )
    fun psHelpFaqTitle(lang: AppLanguage) = get(lang, "FAQ", "FAQ")
    fun psHelpFaqSub(lang: AppLanguage) = get(lang, "Ответы на частые вопросы.", "Answers to common questions.")
    fun psHelpFeedbackTitle(lang: AppLanguage) = get(lang, "Обратная связь", "Feedback")
    fun psHelpFeedbackSub(lang: AppLanguage) = get(lang, "Написать команде.", "Message the team.")

    // Legacy strings kept for screens not yet rewritten (steps 3+).
    fun modeTopic(lang: AppLanguage) = get(lang, "Разбор материала", "Topic Analysis")
    fun modeExam(lang: AppLanguage) = get(lang, "Подготовка к экзамену", "Exam Prep")
    fun matText(lang: AppLanguage) = get(lang, "Вставить текст", "Paste Text")
    fun matPhoto(lang: AppLanguage) = get(lang, "Загрузить фото", "Upload Photo")
    fun matLink(lang: AppLanguage) = get(lang, "Добавить ссылку", "Add Link")
    fun subjectLabel(lang: AppLanguage) = get(lang, "Предмет", "Subject")
    fun selectSubject(lang: AppLanguage) = get(lang, "Выбрать предмет", "Select Subject")
    fun topicPlaceholder(lang: AppLanguage) = get(lang, "Опиши тему или вставь материал", "Describe topic or paste material")
    fun textPlaceholder(lang: AppLanguage) = get(lang, "Вставь текст конспекта, статьи или задачи...", "Paste notes, article, or task text here...")
    fun photoChosen(lang: AppLanguage) = get(lang, "Фотография выбрана (глава учебника / конспект)", "Photo selected (textbook chapter / notes)")
    fun photoChooseBtn(lang: AppLanguage) = get(lang, "Выбрать фото с устройства", "Choose photo from device")
    fun linkPlaceholder(lang: AppLanguage) = get(lang, "Вставь ссылку на статью, PDF или видео...", "Paste link to article, PDF, or video...")
    fun startAiAnalysis(lang: AppLanguage) = get(lang, "Начать разбор с ИИ", "Start AI Analysis")
    fun createProjectHeader(lang: AppLanguage) = get(lang, "НОВЫЙ ПРОЕКТ", "NEW PROJECT")
    fun projectTitlePlaceholder(lang: AppLanguage) = get(lang, "Название проекта (например: ОГЭ по физике)", "Project title (e.g. Physics Exam)")
    fun deadlinePlaceholder(lang: AppLanguage) = get(lang, "Дедлайн (например: 14 дней)", "Deadline (e.g. 14 days)")
    fun createProjectBtn(lang: AppLanguage) = get(lang, "Создать проект", "Create Project")
    fun registryBtn(lang: AppLanguage) = get(lang, "Реестр техник", "Technique Registry")
    fun tryTechnique(lang: AppLanguage) = get(lang, "Попробовать", "Try")

    // Projects Screen
    fun subtabActive(lang: AppLanguage) = get(lang, "АКТИВНЫЕ", "ACTIVE")
    fun subtabHistory(lang: AppLanguage) = get(lang, "ИСТОРИЯ", "HISTORY")
    fun sortByLabel(lang: AppLanguage) = get(lang, "Сортировка:", "Sort by:")
    fun sortByDeadline(lang: AppLanguage) = get(lang, "По дедлайну", "By deadline")
    fun sortByMaterials(lang: AppLanguage) = get(lang, "По материалам", "By materials")
    fun sortByProgress(lang: AppLanguage) = get(lang, "По прогрессу", "By progress")
    fun pinnedHeader(lang: AppLanguage) = get(lang, "ПРИОРИТЕТНЫЕ", "PINNED")
    fun regularHeader(lang: AppLanguage) = get(lang, "ВСЕ ПРОЕКТЫ", "ALL PROJECTS")
    fun convertToProject(lang: AppLanguage) = get(lang, "Передать в проект", "Convert to project")
    fun delete(lang: AppLanguage) = get(lang, "Удалить", "Delete")

    // Project Detail Screen
    fun back(lang: AppLanguage) = get(lang, "Назад", "Back")
    fun projectOptions(lang: AppLanguage) = get(lang, "Опции проекта", "Project options")
    fun changeDeadline(lang: AppLanguage) = get(lang, "Изменить дедлайн", "Change deadline")
    fun resume(lang: AppLanguage) = get(lang, "Возобновить", "Resume")
    fun pause(lang: AppLanguage) = get(lang, "Приостановить", "Pause")
    fun unpin(lang: AppLanguage) = get(lang, "Снять приоритет", "Unpin")
    fun pin(lang: AppLanguage) = get(lang, "Поставить приоритет", "Pin priority")
    fun deleteProject(lang: AppLanguage) = get(lang, "Удалить проект", "Delete project")
    fun materialsHeader(count: Int, lang: AppLanguage) = get(lang, "МАТЕРИАЛЫ ($count)", "MATERIALS ($count)")
    fun addMaterial(lang: AppLanguage) = get(lang, "Добавить материал", "Add material")
    fun beforeDeadline(lang: AppLanguage) = get(lang, "ДО ДЕДЛАЙНА", "BEFORE DEADLINE")
    fun sessionsLeft(lang: AppLanguage) = get(lang, "Осталось сессий", "Sessions left")
    fun nextSession(lang: AppLanguage) = get(lang, "Следующая", "Next session")
    fun continueLearning(lang: AppLanguage) = get(lang, "Продолжить обучение", "Continue Learning")

    // About Screen
    fun statsTab(lang: AppLanguage) = get(lang, "СТАТИСТИКА", "STATS")
    fun methodsTab(lang: AppLanguage) = get(lang, "МЕТОДЫ", "METHODS")
    fun obsTab(lang: AppLanguage) = get(lang, "НАБЛЮДЕНИЯ", "OBSERVATIONS")
    fun sessionsThisWeek(lang: AppLanguage) = get(lang, "Сессий за неделю", "Sessions this week")
    fun hoursStudied(lang: AppLanguage) = get(lang, "Часов учёбы", "Hours studied")
    fun masteredTopics(lang: AppLanguage) = get(lang, "Закреплено тем", "Topics mastered")
    fun favoriteMethod(lang: AppLanguage) = get(lang, "Любимая методика", "Favorite method")
    fun methodPriorities(lang: AppLanguage) = get(lang, "Приоритеты методик", "Method Priorities")
    fun aiNotesTitle(lang: AppLanguage) = get(lang, "Заметки ИИ о твоей учёбе", "AI Study Notes")

    // Session Screen
    fun closeSession(lang: AppLanguage) = get(lang, "Закрыть сессию", "Close session")
    fun aiAnalyzingTitle(lang: AppLanguage) = get(lang, "ИИ разбирает материал", "AI Analyzing Material")
    fun aiCompleteTitle(lang: AppLanguage) = get(lang, "ИИ завершил анализ материала", "AI Analysis Complete")
    fun chooseMethodAndPlan(lang: AppLanguage) = get(lang, "Выбери методику обучения и формат практики:", "Select study technique & practice plan:")
    fun studyTechniqueHeader(lang: AppLanguage) = get(lang, "МЕТОДИКА ОБУЧЕНИЯ", "STUDY TECHNIQUE")
    fun planIntensityHeader(lang: AppLanguage) = get(lang, "ПЛАН И ИНТЕНСИВНОСТЬ", "PLAN & INTENSITY")
    fun startPracticeBtn(lang: AppLanguage) = get(lang, "Начать разбор", "Start Practice")
    fun stepWord(lang: AppLanguage) = get(lang, "Шаг", "Step")
    fun ofWord(lang: AppLanguage) = get(lang, "из", "of")
    fun greatJob(lang: AppLanguage) = get(lang, "Отличная работа!", "Great Job!")
    fun finishSession(lang: AppLanguage) = get(lang, "Завершить сессию", "Finish Session")
    fun nextWord(lang: AppLanguage) = get(lang, "Дальше", "Next")

    // Profile Screen
    fun profileTitle(lang: AppLanguage) = get(lang, "Профиль", "Profile")
    fun sectionAccount(lang: AppLanguage) = get(lang, "АККАУНТ", "ACCOUNT")
    fun sectionStudy(lang: AppLanguage) = get(lang, "УЧЁБА", "STUDY")
    fun sectionDataView(lang: AppLanguage) = get(lang, "ДАННЫЕ И ВИД", "DATA & APPEARANCE")
    fun sectionHelp(lang: AppLanguage) = get(lang, "ПОМОЩЬ", "HELP")
    fun accountTitle(lang: AppLanguage) = get(lang, "Аккаунт", "Account")
    fun accountSub(lang: AppLanguage) = get(lang, "Email, подписка, привязки", "Email, subscription, links")
    fun studyScheduleTitle(lang: AppLanguage) = get(lang, "Занятия", "Schedule")
    fun studyScheduleSub(lang: AppLanguage) = get(lang, "Время сессий, длина, дни без работы", "Session times, duration, rest days")
    fun focusModeTitle(lang: AppLanguage) = get(lang, "Фокус-режим", "Focus Mode")
    fun focusModeSub(lang: AppLanguage) = get(lang, "Блокировка приложений на сессиях", "App blocker during study sessions")
    fun privacyTitle(lang: AppLanguage) = get(lang, "Приватность", "Privacy")
    fun privacySub(lang: AppLanguage) = get(lang, "Экспорт данных, использование ответов", "Data export, response usage")
    fun languageTitle(lang: AppLanguage) = get(lang, "Язык интерфейса", "Interface Language")
    fun languageSub(lang: AppLanguage) = get(lang, "Переключение между Русский и English", "Switch between English & Russian")
    fun appearanceTitle(lang: AppLanguage) = get(lang, "Внешний вид", "Appearance")
    fun darkThemeDesc(lang: AppLanguage) = get(lang, "Тёмная тема (Sapfit)", "Dark Theme (Sapfit)")
    fun lightThemeDesc(lang: AppLanguage) = get(lang, "Светлая тема (Чистый и контрастный)", "Light Theme (Clean & Bright)")
    fun helpTitle(lang: AppLanguage) = get(lang, "Помощь", "Help")
    fun helpSub(lang: AppLanguage) = get(lang, "FAQ, обратная связь, наука обучения", "FAQ, feedback, science of learning")
}
