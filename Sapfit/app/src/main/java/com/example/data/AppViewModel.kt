package com.example.data

import androidx.lifecycle.ViewModel
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class MainTab {
    HOME, PROJECTS, ABOUT, PROFILE
}

enum class HomeMode {
    QUICK, PROJECT
}

enum class ProjectsSubTab {
    ACTIVE, HISTORY
}

enum class SortBy {
    DEADLINE, MATERIALS, PROGRESS
}

enum class AboutSubTab {
    STATS, METHODS, OBSERVATIONS
}

/**
 * Quick / project session stages (spec §6).
 *  PROCESSING — spinner with rotating phrases
 *  BRANCH     — 3-way familiarity picker (novice / partial / expert)
 *  ATOM       — working through the atoms (branch-specific presentation)
 *  DONE       — completion screen, autosave to history
 */
enum class SessionStage {
    PROCESSING, BRANCH, ATOM, DONE
}

/** Project creation flow stages (spec §7). */
enum class ProjectCreateStage {
    /** Fill in goal / subject / deadline. */
    FORM,
    /** Spinner while the roadmap is being "built". */
    PROCESSING,
    /** Scrollable list of anchor points with a 3-way familiarity toggle per point. */
    CALIBRATION,
    /** Not creating anything — used as the default sentinel. */
    IDLE
}

enum class ProjectGoal { TEST, EXAM, SELF }

/** Profile sub-screens (spec §12). Null = main settings list. */
enum class ProfileSubscreen { ACCOUNT, LESSONS, FOCUS, PRIVACY, APPEARANCE, HELP }

enum class AppLanguage {
    RU, EN
}

/**
 * Wave-1 onboarding step ordering (spec §2).
 *  0  WELCOME       — logo, Google/iCloud, terms + privacy checkboxes
 *  1  PROFILE       — name, age, learning situation
 *  2..11 Q1..Q10    — one question per screen
 *  12 PROCESSING    — "Обрабатываю ответы…" ~2 s
 *  13 OBSERVATIONS  — 3 findings
 *  14 MINI_INTRO    — start of practical mini-session
 *  15 MINI_Q        — one demo prompt
 *  16 MINI_DONE     — mini-session finished
 *  17 DONE          — final "Готово" → main app
 * Skip-demo button (spec §4) is visible on steps 1..13 (welcome and mini-session excluded).
 */
object OnbStep {
    const val WELCOME = 0
    const val PROFILE = 1
    const val Q_FIRST = 2
    const val Q_LAST = 11
    const val PROCESSING = 12
    const val OBSERVATIONS = 13
    const val MINI_INTRO = 14
    const val MINI_Q = 15
    const val MINI_DONE = 16
    const val DONE = 17
    const val TOTAL = 18
    // Question count = Q_LAST - Q_FIRST + 1 = 10.
    fun questionIndex(step: Int): Int = step - Q_FIRST
}

data class AppState(
    val currentTab: MainTab = MainTab.HOME,
    val selectedProjectId: String? = null,
    val isRegistryOpen: Boolean = false,
    val isSessionOpen: Boolean = false,
    val sessionStepIndex: Int = 0,
    val sessionStage: SessionStage = SessionStage.PROCESSING,
    val sessionBranch: Familiarity? = null,
    val sessionAtoms: List<SessionAtom> = emptyList(),
    val sessionAtomIndex: Int = 0,
    val sessionShowExplainer: Boolean = false, // partial branch: after answering try-question, show explanation
    val sessionSelectedOption: Int = -1,
    val selectedTechnique: String = "Метод Фейнмана",
    val selectedPlan: String = "Стандартный (5 шагов)",
    val sessionTopicTitle: String = "Разбор материала",

    // Project creation (spec §7)
    val projectCreateStage: ProjectCreateStage = ProjectCreateStage.IDLE,
    val draftGoal: ProjectGoal? = null,
    val draftAnchors: List<RoadmapAnchor> = emptyList(),

    // Registry — selected technique for the detail modal
    val selectedTechniqueId: String? = null,

    // Profile subscreen (spec §12)
    val profileSubscreen: ProfileSubscreen? = null,
    val prefUseAnswersForTraining: Boolean = false,
    val isDarkTheme: Boolean = true,
    val appLanguage: AppLanguage = AppLanguage.EN,

    // Onboarding wave 1 (spec §2)
    val wave1Completed: Boolean = false,
    val onbStep: Int = OnbStep.WELCOME,
    val onbAnswers: OnboardingAnswers = OnboardingAnswers(),
    val onbMiniAnswer: String = "",

    // Onboarding wave 2 (spec §3) — optional deeper setup
    val wave2Completed: Boolean = false,
    val wave2Started: Boolean = false,
    val wave2Step: Int = 0, // 0=intro 1=subject 2=tech1 3=tech2 4=tech3 5=format 6=config
    val wave2Subject: String = "",
    val wave2TechRatings: Map<String, Int> = emptyMap(), // techId -> 0..3 (0=loved, 3=not for me)
    val wave2FormatTime: String = "",        // morning / evening
    val wave2FormatLength: String = "",       // short / long
    val wave2FormatInterleave: String = "",   // mix / single

    // Home screen state
    val homeMode: HomeMode = HomeMode.QUICK,
    val homeInputText: String = "",
    val homeAttachments: List<Attachment> = emptyList(),
    val isAttachMenuOpen: Boolean = false,
    val homePhraseIndex: Int = 0,

    // New project form (used from Home when mode = PROJECT)
    val newProjectTitle: String = "",
    val newProjectSubject: String = "",
    val newProjectDeadline: String = "",
    val isNewSubjectDropdownOpen: Boolean = false,

    // Reminder banners on Home (auto-dismiss + swipe-up + stacking)
    val reminderBanners: List<ReminderBanner> = listOf(
        ReminderBanner("b1", "ege-physics", "ОГЭ по физике", "2 темы почти закреплены — стоит продолжить"),
        ReminderBanner("b2", "trig", "Тригонометрия", "Не занимались 3 дня")
    ),

    // Explainer-banner tracking: which section tabs the user has already visited
    val visitedTabs: Set<MainTab> = emptySet(),
    val dismissedExplainers: Set<ExplainerSection> = emptySet(),

    // Projects screen state
    val projectsSubTab: ProjectsSubTab = ProjectsSubTab.ACTIVE,
    val isSortDropdownOpen: Boolean = false,
    val sortBy: SortBy = SortBy.DEADLINE,

    val projects: List<Project> = listOf(
        Project(
            id = "ege-physics",
            title = "ОГЭ по физике",
            subject = "Физика",
            deadlineDays = 14,
            materialsCount = 12,
            pinned = true,
            paused = false,
            progress = ProjectProgress(locked = 15, learning = 35, ahead = 50),
            meta = "Механика и законы движения · до 14 августа",
            materialsList = listOf(
                ProjectMaterial("m1", "text", "Конспект: законы Ньютона"),
                ProjectMaterial("m2", "photo", "Фото учебника, глава 4"),
                ProjectMaterial("m3", "link", "Видео-разбор задач на динамику")
            ),
            sessionsLeft = 6,
            nextSession = "завтра, 18:00"
        ),
        Project(
            id = "trig",
            title = "Тригонометрия",
            subject = "Алгебра",
            deadlineDays = null,
            materialsCount = 6,
            pinned = false,
            paused = false,
            progress = ProjectProgress(locked = 34, learning = 20, ahead = 46),
            meta = "Формулы приведения · без дедлайна",
            materialsList = listOf(
                ProjectMaterial("m4", "text", "Шпаргалка формул приведения"),
                ProjectMaterial("m5", "text", "Задачи на тригонометрический круг")
            ),
            sessionsLeft = 4,
            nextSession = "сегодня, 20:00"
        ),
        Project(
            id = "history-xx",
            title = "История: XX век",
            subject = "История",
            deadlineDays = 30,
            materialsCount = 3,
            pinned = false,
            paused = false,
            progress = ProjectProgress(locked = 5, learning = 15, ahead = 80),
            meta = "Первая и Вторая мировые войны · до 25 августа",
            materialsList = listOf(
                ProjectMaterial("m6", "text", "Хронология событий 1914–1918"),
                ProjectMaterial("m7", "link", "Архивные карты фронтов")
            ),
            sessionsLeft = 8,
            nextSession = "пятница, 17:30"
        )
    ),

    val historyItems: List<HistoryItem> = listOf(
        HistoryItem("h1", "Квадратные уравнения", "Сегодня, 09:12", "2 материала · фото конспекта"),
        HistoryItem("h2", "Клеточное дыхание", "Вчера, 21:40", "1 материал · текст"),
        HistoryItem("h3", "Причастный оборот", "3 дня назад", "1 материал · ссылка")
    ),

    // Project Detail menu
    val isProjectMenuOpen: Boolean = false,

    // About screen state
    val aboutSubTab: AboutSubTab = AboutSubTab.STATS,
    val methodPriorities: List<MethodPriority> = listOf(
        MethodPriority("Активное припоминание", 85),
        MethodPriority("Метод Фейнмана", 60),
        MethodPriority("Интерливинг", 35)
    ),
    val observations: List<Observation> = listOf(
        Observation("o1", "2 дня назад", "Заметил, что сессии по алгебре в 22:00 короче и с большим числом ошибок. Перенёс их на утро — точность выросла."),
        Observation("o2", "5 дней назад", "Метод Фейнмана заходит тебе лучше пересказа своими словами — стал использовать его чаще для физики."),
        Observation("o3", "Неделю назад", "Короткие сессии по 15 минут работают стабильнее длинных при высокой загрузке — учёл это в расписании.")
    ),

    // Kept as a legacy no-op field so any lingering references don't break;
    // the new session flow uses sessionAtoms + sessionBranch instead.
    val sessionQuestions: List<SessionQuestion> = emptyList()
)

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    fun selectTab(tab: MainTab) {
        _uiState.update {
            it.copy(
                currentTab = tab,
                selectedProjectId = null,
                isRegistryOpen = false,
                isSortDropdownOpen = false,
                isProjectMenuOpen = false,
                isAttachMenuOpen = false,
                visitedTabs = it.visitedTabs + tab
            )
        }
    }

    fun setHomeMode(mode: HomeMode) {
        _uiState.update { it.copy(homeMode = mode) }
    }

    fun updateHomeInput(text: String) {
        _uiState.update { it.copy(homeInputText = text) }
    }

    fun toggleAttachMenu() {
        _uiState.update { it.copy(isAttachMenuOpen = !it.isAttachMenuOpen) }
    }

    fun closeAttachMenu() {
        _uiState.update { it.copy(isAttachMenuOpen = false) }
    }

    fun addAttachment(kind: AttachmentKind) {
        val label = when (kind) {
            AttachmentKind.CAMERA -> "Photo"
            AttachmentKind.IMAGE -> "Gallery"
            AttachmentKind.FILE -> "File"
        }
        val att = Attachment(
            id = "a_${System.currentTimeMillis()}",
            kind = kind,
            label = label
        )
        _uiState.update {
            it.copy(
                homeAttachments = it.homeAttachments + att,
                isAttachMenuOpen = false
            )
        }
    }

    fun removeAttachment(id: String) {
        _uiState.update { it.copy(homeAttachments = it.homeAttachments.filter { a -> a.id != id }) }
    }

    fun advanceHomePhrase() {
        _uiState.update { it.copy(homePhraseIndex = it.homePhraseIndex + 1) }
    }

    /**
     * Home send button: routes into the right flow based on the selected mode.
     * Everything is mocked — real Gemini calls are not wired up.
     */
    fun sendFromHome() {
        val state = _uiState.value
        val hasPayload = state.homeInputText.isNotBlank() || state.homeAttachments.isNotEmpty()
        if (!hasPayload) return

        when (state.homeMode) {
            HomeMode.QUICK -> {
                val topic = state.homeInputText.ifBlank { "Быстрая сессия" }
                _uiState.update {
                    it.copy(
                        sessionTopicTitle = topic,
                        sessionAtoms = SessionContent.buildAtomsFor(topic),
                        sessionAtomIndex = 0,
                        sessionBranch = null,
                        sessionShowExplainer = false,
                        sessionSelectedOption = -1,
                        isSessionOpen = true,
                        sessionStage = SessionStage.PROCESSING,
                        homeInputText = "",
                        homeAttachments = emptyList(),
                        isAttachMenuOpen = false
                    )
                }
            }
            HomeMode.PROJECT -> {
                _uiState.update {
                    it.copy(
                        newProjectTitle = state.homeInputText,
                        homeInputText = "",
                        isAttachMenuOpen = false,
                        projectCreateStage = ProjectCreateStage.FORM
                    )
                }
            }
        }
    }

    fun updateNewProjectTitle(title: String) {
        _uiState.update { it.copy(newProjectTitle = title) }
    }

    fun setNewProjectSubject(subject: String) {
        _uiState.update { it.copy(newProjectSubject = subject, isNewSubjectDropdownOpen = false) }
    }

    fun toggleNewSubjectDropdown() {
        _uiState.update { it.copy(isNewSubjectDropdownOpen = !it.isNewSubjectDropdownOpen) }
    }

    fun updateNewProjectDeadline(deadline: String) {
        _uiState.update { it.copy(newProjectDeadline = deadline) }
    }

    fun clickReminderBanner(banner: ReminderBanner) {
        _uiState.update {
            it.copy(
                reminderBanners = it.reminderBanners.filter { b -> b.id != banner.id },
                currentTab = MainTab.PROJECTS,
                selectedProjectId = banner.projectId,
                visitedTabs = it.visitedTabs + MainTab.PROJECTS
            )
        }
    }

    fun dismissReminderBanner(bannerId: String) {
        _uiState.update { it.copy(reminderBanners = it.reminderBanners.filter { b -> b.id != bannerId }) }
    }

    fun dismissExplainer(section: ExplainerSection) {
        _uiState.update { it.copy(dismissedExplainers = it.dismissedExplainers + section) }
    }

    fun selectProjectsSubTab(tab: ProjectsSubTab) {
        _uiState.update { it.copy(projectsSubTab = tab, isSortDropdownOpen = false) }
    }

    fun toggleSortDropdown() {
        _uiState.update { it.copy(isSortDropdownOpen = !it.isSortDropdownOpen) }
    }

    fun setSortBy(sortBy: SortBy) {
        _uiState.update { it.copy(sortBy = sortBy, isSortDropdownOpen = false) }
    }

    fun openProjectDetail(id: String) {
        _uiState.update { it.copy(selectedProjectId = id, isProjectMenuOpen = false) }
    }

    fun closeProjectDetail() {
        _uiState.update { it.copy(selectedProjectId = null, isProjectMenuOpen = false) }
    }

    fun toggleProjectMenu() {
        _uiState.update { it.copy(isProjectMenuOpen = !it.isProjectMenuOpen) }
    }

    fun togglePinProject(id: String) {
        _uiState.update { state ->
            state.copy(
                projects = state.projects.map { p ->
                    if (p.id == id) p.copy(pinned = !p.pinned) else p
                },
                isProjectMenuOpen = false
            )
        }
    }

    fun togglePauseProject(id: String) {
        _uiState.update { state ->
            state.copy(
                projects = state.projects.map { p ->
                    if (p.id == id) p.copy(paused = !p.paused) else p
                },
                isProjectMenuOpen = false
            )
        }
    }

    fun deleteProject(id: String) {
        _uiState.update { state ->
            state.copy(
                projects = state.projects.filter { p -> p.id != id },
                selectedProjectId = null,
                isProjectMenuOpen = false
            )
        }
    }

    fun convertHistoryToProject(historyId: String) {
        val item = _uiState.value.historyItems.find { it.id == historyId } ?: return
        val newProj = Project(
            id = "p_${System.currentTimeMillis()}",
            title = item.title,
            subject = "Общее",
            deadlineDays = null,
            materialsCount = 1,
            pinned = false,
            paused = false,
            progress = ProjectProgress(locked = 0, learning = 15, ahead = 85),
            meta = item.preview,
            materialsList = listOf(ProjectMaterial("m_hist", "text", item.preview)),
            sessionsLeft = 5,
            nextSession = "—"
        )
        _uiState.update {
            it.copy(
                historyItems = it.historyItems.filter { h -> h.id != historyId },
                projects = listOf(newProj) + it.projects,
                currentTab = MainTab.PROJECTS,
                projectsSubTab = ProjectsSubTab.ACTIVE,
                selectedProjectId = newProj.id
            )
        }
    }

    fun deleteHistoryItem(id: String) {
        _uiState.update { it.copy(historyItems = it.historyItems.filter { h -> h.id != id }) }
    }

    fun selectAboutSubTab(tab: AboutSubTab) {
        _uiState.update { it.copy(aboutSubTab = tab) }
    }

    fun openRegistry() {
        _uiState.update { it.copy(isRegistryOpen = true) }
    }

    fun closeRegistry() {
        _uiState.update { it.copy(isRegistryOpen = false, selectedTechniqueId = null) }
    }

    fun openTechniqueDetail(id: String) {
        _uiState.update { it.copy(selectedTechniqueId = id) }
    }

    fun closeTechniqueDetail() {
        _uiState.update { it.copy(selectedTechniqueId = null) }
    }

    fun openProfileSubscreen(sub: ProfileSubscreen) {
        _uiState.update { it.copy(profileSubscreen = sub) }
    }

    fun closeProfileSubscreen() {
        _uiState.update { it.copy(profileSubscreen = null) }
    }

    fun togglePrefUseAnswersForTraining() {
        _uiState.update { it.copy(prefUseAnswersForTraining = !it.prefUseAnswersForTraining) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun toggleLanguage() {
        _uiState.update {
            it.copy(appLanguage = if (it.appLanguage == AppLanguage.RU) AppLanguage.EN else AppLanguage.RU)
        }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(appLanguage = language) }
    }

    // ==================== Quick / project session (spec §6) ====================

    fun openSession(topic: String? = null) {
        val sessionTopic = topic ?: "Ньютоновская механика"
        _uiState.update {
            it.copy(
                sessionTopicTitle = sessionTopic,
                sessionAtoms = SessionContent.buildAtomsFor(sessionTopic),
                sessionAtomIndex = 0,
                sessionBranch = null,
                sessionShowExplainer = false,
                sessionSelectedOption = -1,
                isSessionOpen = true,
                sessionStage = SessionStage.PROCESSING
            )
        }
    }

    /** Spinner is done — move to the 3-way familiarity picker (spec §6 шаг 3). */
    fun finishSessionProcessing() {
        _uiState.update { it.copy(sessionStage = SessionStage.BRANCH) }
    }

    fun pickSessionBranch(f: Familiarity) {
        _uiState.update {
            it.copy(
                sessionBranch = f,
                sessionStage = SessionStage.ATOM,
                sessionAtomIndex = 0,
                sessionSelectedOption = -1,
                // In "не знаком" — start with the explainer visible.
                // In "немного знаю" — start with the try-question visible.
                // In "знаком, хочу повторить" — start with the direct question.
                sessionShowExplainer = f == Familiarity.NOVICE
            )
        }
    }

    fun selectSessionOption(index: Int) {
        _uiState.update { it.copy(sessionSelectedOption = index) }
    }

    /**
     * "Далее" inside an atom:
     * - novice: explainer → option check → next atom
     * - partial: try-question → explainer → next atom
     * - expert: direct question → next atom
     */
    fun advanceInAtom() {
        val s = _uiState.value
        val branch = s.sessionBranch ?: return

        // In partial branch, after answering the try-question we still need to show the explainer.
        if (branch == Familiarity.PARTIAL && !s.sessionShowExplainer) {
            _uiState.update { it.copy(sessionShowExplainer = true) }
            return
        }

        val nextIdx = s.sessionAtomIndex + 1
        if (nextIdx >= s.sessionAtoms.size) {
            _uiState.update { it.copy(sessionStage = SessionStage.DONE) }
        } else {
            _uiState.update {
                it.copy(
                    sessionAtomIndex = nextIdx,
                    sessionSelectedOption = -1,
                    sessionShowExplainer = branch == Familiarity.NOVICE
                )
            }
        }
    }

    fun finishSession() {
        val s = _uiState.value
        val newHistoryItem = HistoryItem(
            id = "h_${System.currentTimeMillis()}",
            title = s.sessionTopicTitle,
            date = "Только что",
            preview = "${s.sessionAtoms.size} тем · авто-сохранено"
        )
        _uiState.update {
            it.copy(
                isSessionOpen = false,
                sessionStage = SessionStage.PROCESSING,
                sessionAtomIndex = 0,
                sessionBranch = null,
                sessionShowExplainer = false,
                sessionSelectedOption = -1,
                historyItems = listOf(newHistoryItem) + it.historyItems
            )
        }
    }

    fun closeSession() {
        _uiState.update {
            it.copy(
                isSessionOpen = false,
                sessionStage = SessionStage.PROCESSING,
                sessionAtomIndex = 0,
                sessionBranch = null,
                sessionShowExplainer = false,
                sessionSelectedOption = -1
            )
        }
    }

    // ==================== Project creation flow (spec §7) ====================

    fun beginProjectCreate() {
        _uiState.update {
            it.copy(
                projectCreateStage = ProjectCreateStage.FORM,
                newProjectTitle = it.newProjectTitle.ifBlank { it.homeInputText },
                homeInputText = "",
                homeAttachments = emptyList()
            )
        }
    }

    fun setDraftGoal(goal: ProjectGoal) {
        _uiState.update { it.copy(draftGoal = goal) }
    }

    fun submitProjectDraft() {
        val s = _uiState.value
        if (s.newProjectTitle.isBlank()) return
        _uiState.update { it.copy(projectCreateStage = ProjectCreateStage.PROCESSING) }
    }

    /** Called from the processing screen when the mock spinner is done. */
    fun finishProjectProcessing() {
        val s = _uiState.value
        val anchors = SessionContent.buildAnchorsFor(s.newProjectTitle, s.newProjectSubject.ifBlank { "Общее" })
        _uiState.update {
            it.copy(
                draftAnchors = anchors,
                projectCreateStage = ProjectCreateStage.CALIBRATION
            )
        }
    }

    fun setAnchorFamiliarity(anchorId: String, f: Familiarity) {
        _uiState.update {
            it.copy(
                draftAnchors = it.draftAnchors.map { a ->
                    if (a.id == anchorId) a.copy(familiarity = f) else a
                }
            )
        }
    }

    /** Finalise draft → create Project → jump to its detail page. */
    fun finalizeProjectCreation() {
        val s = _uiState.value
        val title = s.newProjectTitle.trim()
        if (title.isBlank()) return
        val subject = s.newProjectSubject.ifBlank { "Общее" }
        val goalLabel = when (s.draftGoal) {
            ProjectGoal.TEST -> "Контрольная"
            ProjectGoal.EXAM -> "Экзамен"
            ProjectGoal.SELF -> "Освоение материала"
            null -> "Освоение материала"
        }
        val deadline = s.newProjectDeadline
        val newProj = Project(
            id = "p_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            deadlineDays = if (deadline.isNotBlank()) 14 else null,
            materialsCount = maxOf(1, s.draftAnchors.size / 2),
            pinned = false,
            paused = false,
            progress = ProjectProgress(locked = 0, learning = 15, ahead = 85),
            meta = "$subject · $goalLabel" + if (deadline.isNotBlank()) " · до $deadline" else "",
            materialsList = listOf(ProjectMaterial("m_new", "text", "Материалы из карточки создания")),
            sessionsLeft = s.draftAnchors.size,
            nextSession = "завтра"
        )
        _uiState.update {
            it.copy(
                projects = listOf(newProj) + it.projects,
                projectCreateStage = ProjectCreateStage.IDLE,
                draftGoal = null,
                draftAnchors = emptyList(),
                newProjectTitle = "",
                newProjectSubject = "",
                newProjectDeadline = "",
                currentTab = MainTab.PROJECTS,
                projectsSubTab = ProjectsSubTab.ACTIVE,
                selectedProjectId = newProj.id
            )
        }
    }

    fun cancelProjectCreation() {
        _uiState.update {
            it.copy(
                projectCreateStage = ProjectCreateStage.IDLE,
                draftGoal = null,
                draftAnchors = emptyList(),
                newProjectTitle = "",
                newProjectSubject = "",
                newProjectDeadline = ""
            )
        }
    }

    fun addMaterialToProject(projectId: String, name: String, iconType: String = "text") {
        _uiState.update { st ->
            st.copy(
                projects = st.projects.map { p ->
                    if (p.id == projectId) {
                        p.copy(
                            materialsList = p.materialsList + ProjectMaterial("m_${System.currentTimeMillis()}", iconType, name),
                            materialsCount = p.materialsCount + 1
                        )
                    } else p
                }
            )
        }
    }

    // ==================== Onboarding wave 1 (spec §2) ====================

    fun updateOnbAnswers(transform: (OnboardingAnswers) -> OnboardingAnswers) {
        _uiState.update { it.copy(onbAnswers = transform(it.onbAnswers)) }
    }

    fun updateOnbMiniAnswer(text: String) {
        _uiState.update { it.copy(onbMiniAnswer = text) }
    }

    fun onbNext() {
        _uiState.update {
            val next = (it.onbStep + 1).coerceAtMost(OnbStep.DONE)
            it.copy(onbStep = next)
        }
    }

    fun onbBack() {
        _uiState.update {
            val prev = (it.onbStep - 1).coerceAtLeast(OnbStep.WELCOME)
            it.copy(onbStep = prev)
        }
    }

    fun onbGoTo(step: Int) {
        _uiState.update { it.copy(onbStep = step.coerceIn(OnbStep.WELCOME, OnbStep.DONE)) }
    }

    fun onbFinish() {
        _uiState.update { it.copy(wave1Completed = true, onbStep = OnbStep.DONE) }
    }

    /**
     * Spec §4 — jury-only "Skip (demo)" button.
     * Fills in a plausible demo profile so the main app isn't empty,
     * then jumps straight to Home.
     */
    // ==================== Onboarding wave 2 (spec §3) ====================

    fun startWave2() {
        _uiState.update { it.copy(wave2Started = true, wave2Step = 0) }
    }

    fun wave2Cancel() {
        _uiState.update { it.copy(wave2Started = false) }
    }

    fun wave2Next() {
        _uiState.update {
            val next = (it.wave2Step + 1).coerceAtMost(7)
            it.copy(wave2Step = next)
        }
    }

    fun wave2Back() {
        _uiState.update {
            val prev = (it.wave2Step - 1).coerceAtLeast(0)
            it.copy(wave2Step = prev)
        }
    }

    fun setWave2Subject(subject: String) {
        _uiState.update { it.copy(wave2Subject = subject) }
    }

    fun rateWave2Tech(techId: String, rating: Int) {
        _uiState.update { it.copy(wave2TechRatings = it.wave2TechRatings + (techId to rating)) }
    }

    fun setWave2FormatTime(v: String) = _uiState.update { it.copy(wave2FormatTime = v) }
    fun setWave2FormatLength(v: String) = _uiState.update { it.copy(wave2FormatLength = v) }
    fun setWave2FormatInterleave(v: String) = _uiState.update { it.copy(wave2FormatInterleave = v) }

    fun wave2Finish() {
        _uiState.update { it.copy(wave2Completed = true, wave2Started = false, wave2Step = 0) }
    }

    fun onbSkipDemo() {
        _uiState.update {
            it.copy(
                wave1Completed = true,
                onbStep = OnbStep.DONE,
                onbAnswers = OnboardingAnswers(
                    agreedTerms = true,
                    agreedPrivacy = true,
                    name = "Alex",
                    age = "16",
                    situation = "exam",
                    q1Sleep = 1,      // 5–6 h
                    q2Screen = 2,     // 3–5 h
                    q3Focus = 2,      // often
                    q4Start = 2,      // usually slow to start
                    q5PrepStyle = 0,  // re-reads
                    q6WhyClear = 2,   // rather not
                    q7Recall = 2,     // often
                    q8Hours = 1,      // 1–2 h
                    q9Feeling = 1,    // a bit tense
                    q10Free = ""
                )
            )
        }
    }
}
