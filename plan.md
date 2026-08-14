Halo AI — Implementation Plan

Problem statement
- Build a production-ready Android app named "Halo AI" in Kotlin using Jetpack Compose, following Clean Architecture and MVVM, with an Apple HIG-inspired glassmorphism UI, multimodal AI features, hardware-aware optimizations, and assistant-level system integration.

High-level approach
- Modular multi-module Gradle project: app (UI), core (common utilities, theme, DI), data (API clients, local persistence), domain (use-cases, models), feature modules (chat, voice, vision).
- Clean Architecture: domain -> data -> app, with MVVM at feature surface using StateFlow and compose UI.
- Design system: single Theme.kt implementing Apple HIG-style glassmorphism, SF-like typography, squircles, and spring physics. Hardware-aware blur fallbacks.
- AI Router: coroutine-based failover wrapper with providers for Google AI Studio, Groq, OpenRouter; abstracted behind an interface for testing and mocking.
- Voice: Android SpeechRecognizer and TextToSpeech adapters, VoiceInteractionService registration for assistant integration.
- Persistence: Room (conversations, messages) + DataStore for settings and secure BuildConfig/.env for keys placeholders.
- Testing: unit tests for domain and router failover, UI tests for key flows, performance profiling for Low-End optimization.

Progress (2026-08-14)
- Skeleton created (multi-module placeholders, manifest, gradle files)
- Theme scaffolded with HaloTheme and haloSpring
- SplashScreen implemented: glowing multi-color halo, spring-based scale, fade to ChatScreen
- ChatScreen created and wired to mocked AiRouter
- Mock AI providers created: MockGoogleAiClient, MockGroqClient, MockOpenRouterClient
- ChatViewModel implemented and updated to persist messages to Room (AppDatabase + DAO + Entities)
- GlassCard updated to apply RenderEffect on API 31+ using AndroidView host; acrylic fallback used otherwise
- Markdown renderer added (minimal support for paragraphs and fenced code blocks)
- VoiceService scaffolding added (STT/TTS wrappers)
- HardwareUtils added for RenderEffect and low-RAM detection
- Onboarding composable added to explain permissions

Next steps
- Visual polish & HIG alignment: typography weights, spacing, continuous-curve squircles, subtle border strokes, and motion timing refinement
- Full markdown renderer with syntax highlighting and selectable code blocks (consider integrating a light syntax-highlighter lib)
- Full multimodal vision: Android Photo Picker integration, camera capture, OCR pipeline (ML Kit or Tesseract), and VQA flow
- Voice Assistant overlay: implement VoiceInteractionService, floating overlay with animated gradient stroke and waveform visualizer; handle SYSTEM_ALERT_WINDOW and assistant binding flows
- Low-End profiling & optimization: ensure acrylic fallbacks maintain 60 FPS, add derivedStateOf usage to prevent recompositions, and lazy list recycling
- Security & keys: implement BuildConfig injection and .env examples (do not commit keys); add instructions for Play Store compliance
- Testing & CI: unit tests for AiRouter failover, UI tests for splash->chat, CI script

Todos status
- ui-design-implementation: in_progress
- glassmorphism-theme: in_progress
- main-screen-chat: in_progress
- ai-failover-router: in_progress
- ai-clients-adapters: in_progress
- voice-assistant-integration: in_progress
- multimodal-vision: in_progress
- persistence-chat-history: done (scaffolded and basic persistence wired)
- low-end-optimization: in_progress
- permissions-and-onboarding: in_progress
- splash-animation: done
- chat-ui-mock: done

Notes
- API keys: using mocked adapters for initial implementation. Real integrations are pluggable and keys should be provided via secure build-time injection or a secure keystore.
- Accessibility: ensure TalkBack support and large text modes; will iterate after visual polish.
- Play Store: overlays and default assistant bindings require careful UX and privacy disclosures; prepare documentation for the privacy policy and feature consent.

Next action
- Continue with visual polish: refine Theme tokens, GlassCard strokes and blur strengths; then implement richer Markdown capabilities and finish UI polish. If approved, will proceed now.
