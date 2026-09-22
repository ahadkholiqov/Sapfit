package com.example.model

data class ProjectProgress(
    val locked: Int,
    val learning: Int,
    val ahead: Int
)

data class ProjectMaterial(
    val id: String,
    val iconType: String, // "text", "photo", "link"
    val name: String
)

data class Project(
    val id: String,
    val title: String,
    val subject: String,
    val deadlineDays: Int?,
    val materialsCount: Int,
    val pinned: Boolean,
    val paused: Boolean,
    val progress: ProjectProgress,
    val meta: String,
    val materialsList: List<ProjectMaterial>,
    val sessionsLeft: Int,
    val nextSession: String
)

data class HistoryItem(
    val id: String,
    val title: String,
    val date: String,
    val preview: String
)

data class MethodPriority(
    val name: String,
    val percentage: Int
)

data class Observation(
    val id: String,
    val date: String,
    val text: String
)

data class Technique(
    val id: String,
    val name: String,
    val desc: String,
    val tag: String,
    val isDemo: Boolean
)

data class SessionQuestion(
    val id: Int,
    val prompt: String,
    val inputPlaceholder: String? = null,
    val isCompletion: Boolean = false
)

/** Familiarity level chosen by the user (spec §6 shag 3, §7 shag 4). */
enum class Familiarity { NOVICE, PARTIAL, EXPERT }

/**
 * One atom of a session — the smallest step the user works through.
 * In the mockup each atom carries all three possible presentations
 * (explanation, try-question, expert-question) and the current branch
 * picks which one to show.
 */
data class SessionAtom(
    val id: Int,
    val title: String,
    val explainer: String,
    val tryQuestion: String,
    val options: List<String>,
    val correctIndex: Int,
    val expertQuestion: String
)

/** A single anchor point in a project's roadmap (spec §7). */
data class RoadmapAnchor(
    val id: String,
    val title: String,
    val familiarity: Familiarity = Familiarity.NOVICE,
    val done: Boolean = false
)

enum class AttachmentKind { CAMERA, IMAGE, FILE }

data class Attachment(
    val id: String,
    val kind: AttachmentKind,
    val label: String
)

data class ReminderBanner(
    val id: String,
    val projectId: String,
    val title: String,
    val text: String
)

enum class ExplainerSection { PROJECTS, ABOUT, PROFILE }

data class ExplainerBanner(
    val section: ExplainerSection,
    val title: String,
    val text: String
)

data class OnboardingAnswers(
    // Screen 1 — consent
    val agreedTerms: Boolean = false,
    val agreedPrivacy: Boolean = false,
    // Screen 2 — profile
    val name: String = "",
    val age: String = "",
    val situation: String = "",
    // Screens 3–12 — 10 questions (index of chosen option; -1 = not answered)
    val q1Sleep: Int = -1,
    val q2Screen: Int = -1,
    val q3Focus: Int = -1,
    val q4Start: Int = -1,
    val q5PrepStyle: Int = -1,
    val q6WhyClear: Int = -1,
    val q7Recall: Int = -1,
    val q8Hours: Int = -1,
    val q9Feeling: Int = -1,
    val q10Free: String = ""
)
