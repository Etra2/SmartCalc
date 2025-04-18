// Funkcja obsługująca przyciski na ekranie
function addDigit(char) {
    const input = document.getElementById('expression');
    input.value += char;
}

// Funkcja czyszcząca pole wejściowe
function clearInput() {
    const input = document.getElementById('expression');
    input.value = '';
}

// Całość uruchamiana po załadowaniu strony
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('expression');

    // Obsługuje klawisze fizyczne
    document.addEventListener('keydown', (event) => {
        const allowedKeys = '0123456789+-*/().%';

        // Dozwolone znaki
        if (allowedKeys.includes(event.key)) {
            event.preventDefault();

            if (event.key === '.' && !input.value.includes('.')) {
                input.value += event.key;
            } else if (event.key !== '.') {
                input.value += event.key;
            }
        }

        // Zamiana przecinka na kropkę
        if (event.key === ',') {
            event.preventDefault();
            input.value += '.';
        }

        // Enter = oblicz
        if (event.key === 'Enter') {
            event.preventDefault();
            document.querySelector('.calculator-form').submit();
        }

        // Backspace = usuń ostatni znak
        if (event.key === 'Backspace') {
            event.preventDefault();
            input.value = input.value.slice(0, -1);
        }

        // Escape = wyczyść
        if (event.key === 'Escape') {
            event.preventDefault();
            clearInput();
        }
    });

    // Przycisk przejścia do kalkulatora naukowego
    const scientificBtn = document.getElementById('scientificCalcTab');
    if (scientificBtn) {
        scientificBtn.addEventListener('click', () => {
            window.location.href = '/scientific';
        });
    }
});
