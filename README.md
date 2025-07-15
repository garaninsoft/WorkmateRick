# WorkmateRick 🌐

**WorkmateRick** — это Android MVP-приложение для поиска и просмотра персонажей из вселенной *Rick and Morty*, созданное в рамках pet-проекта.

## ✨ Возможности

- 🔍 Поиск персонажей по имени
- 🛠️ Фильтрация по статусу, полу и виду
- 📂 Экран деталей персонажа
- ⬇️ Автоматическая пагинация при скролле
- 🔄 Pull-to-refresh и offline-режим на Room

## 🚀 Технологии

- **Jetpack Compose** UI
- **Hilt** DI
- **Retrofit + Gson** для сети
- **Room** для offline-режима
- **Kotlin Flow** для реактивности
- **Single-Activity** + Navigation Compose

## ⚖️ Архитектура

- MVVM
- Clean Architecture
- DI через Hilt

### 🌐 Слои:

```
- presentation.screen (Compose UI + ViewModel)
- domain.model / repository (Entity + Interface)
- data.model / api / repository.impl / local (DTO, API, Room, mapping)
- di (Modules)
```

## 📚 Старт проекта

```bash
git clone https://github.com/<your-name>/WorkmateRick.git
open in Android Studio (Hedgehog+)
build & run
```

## ✅ Статус

**MVP завершён** — проект полностью соответствует ТЗ:

-

## 🙌 Планы (по желанию)

- Bookmark персонажей
- Работа с API через Paging 3
- Тесты (unit, UI)
- CI/CD + Play Store

---

Создано с любовью и Kotlin ❤️ by @garaninsoft

