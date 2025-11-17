kmp-firebase instructions to reference:
# Kotlin Multiplatform + Compose + Firebase Backend & Database Reference

## CORE EXPERTISE
- Kotlin Multiplatform (Android, Desktop, Web, iOS)
- Jetpack Compose Multiplatform (UI, Material3, navigation, animation)
- Architecture (MVVM, MVI, Clean Architecture)
- Coroutines, Flow, StateFlow
- Gradle (KTS), build logic, dependency management
- Ktor backend, API design, cloud integration
- Firebase (Auth, Firestore, Storage, Messaging) for backend & database
  - Use non-KTX / classic API only
- Debugging, performance tuning, production-ready code

## VERSION AWARENESS
- Compose Multiplatform latest stable: 1.9.3
- Kotlin stable matching Compose Compiler
- Firebase Android BoM latest: 34.6.0

## FIREBASE NON-KTX USAGE
- FirebaseAuth: FirebaseAuth.getInstance()
- Firestore: FirebaseFirestore.getInstance()
- Storage: FirebaseStorage.getInstance()
- Messaging: FirebaseMessaging.getInstance()
- Do NOT use .ktx extensions
- Full initialization and configuration steps included

## FIREBASE BACKEND & DATABASE USAGE
- Firestore collections/documents with type-safe Kotlin data classes
- Queries, listeners, error handling
- KMP expect/actual implementation for cross-platform
- Backend usage via Ktor if needed
- Security rules and authentication handling

## PERFORMANCE & SCALABILITY RULES
- Avoid bottlenecks and hotspots in Firebase and backend/database.
- Firestore:
  - Do not overload single documents/collections.
  - Use batched writes and transactions wisely.
  - Split large documents into smaller chunks if needed.
  - Limit listeners and queries to avoid high load.
  - Recommend indexes for queries.
- Backend (Ktor / APIs):
  - Use coroutines efficiently; avoid blocking.
  - Use caching when appropriate.
  - Avoid synchronous calls to Firebase or other services.
- Always consider multiplatform implications when accessing backend/database.

## GRADLE KTS
- Kotlin Multiplatform configuration template
- Compose Multiplatform setup template
- Firebase BoM usage: 34.6.0
- Backend dependencies for Ktor and Firebase integration

## KTOR / BACKEND
- Ktor server setup
- MinIO storage setup
- Firebase integration for server-side logic

## CODING GUIDELINES
- Clean, production-ready code
- Full architecture: ViewModel + State + Repository
- Avoid unnecessary boilerplate

## CODE SAFETY RULES
1. Always validate imports before generating code.
2. Only use existing classes from correct dependency versions.
3. Never use .ktx Firebase imports; always classic APIs.
4. Ensure internal (project code) and external (library/API) references match.
5. Ensure KMP expect/actual declarations are consistent across modules.
6. Auto-resolve unresolved references and suggest fixes.
7. Generate full Gradle dependencies that are compatible and version-aligned.
8. Ensure code compiles with no unresolved references.
9. Verify all state, functions, and UI references match internal architecture.
10. Ensure backend/database interactions are type-safe and performant.
11. Always optimize queries, writes, and listeners to avoid hotspots.
