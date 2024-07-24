function tryToGuess(session, userInput) {
    if (isLetter(userInput)) {
        tryToGuessLetter(session, userInput)
    } else {
        tryToGuessWord(session, userInput)
    }
}

function tryToGuessWord(session, userWord) {
    if (userWord == session.word) {
        win(session)
    } else {
        session.answer = "Увы, вы не угадали...\n Слово: " + formatMask(session.mask)
    }
    decrementTriesToGuess(session)
} 

function tryToGuessLetter(session, userLetter) {
    var haveNotLettersToGuess = true;
    var i = 0
    while (i < session.word.length) {
        if (session.word[i] == userLetter) {
            session.mask[i] = userLetter
            session.correctLetter = true
        }
        if (session.mask[i] == "_") {
            haveNotLettersToGuess = false
        }
        i++
    }
    
    if (haveNotLettersToGuess) {
        win(session)
    } else if (session.correctLetter) {
        session.answer = "Отлично! Вы угадали букву!\nСлово: " 
    } else {
        session.answer = "Сожалею, но ответ неверный.\nСлово: " 
    }
    session.answer = session.answer + formatMask(session.mask)
    
    decrementTriesToGuess(session)
}

function decrementTriesToGuess(session) {
    session.triesToGuess--
    
    if (session.triesToGuess == 2) {
        session.answer = "У вас осталось 2 попытки.\n" + session.answer
    } else if (session.triesToGuess == 1) {
        session.answer = "У вас осталась 1 попытка.\n\n" + session.answer
    } else if (session.triesToGuess == 0) {
        lose(session)
    }
}

function win(session) {
    session.answer = "Поздравяю, вы выиграли!\nСлово: "
    session.startGame = false
}

function lose(session) {
    session.answer = "Увы, вы проиграли!\nВаше слово было: " + session.word
    session.startGame = false
}

function initSession(session, hangmanGameData) {
    session.word = chooseRandWord(hangmanGameData)
    session.mask = createMask(session.word)
    session.triesToGuess = 6
    session.correctLetter = false
    session.startGame = true
    session.answer = "Кол-во букв в вашем слове: " + session.word.length + "\nСлово: " + formatMask(session.mask)
    // session.answer = session.answer + ".\nПодсказка: " + session.word
}

function isLetter(userInput) {
    return userInput.length == 1
}

function formatMask(mask) {
    return mask.join(" ")
}

function createMask(word) {
    var sym = "_"
    var mask = new Array(word.length)
    var i = 0;
    while (i < word.length) {
        mask[i] = sym
        i++
    }
    return mask
}

function chooseRandWord(words) {
    var wordId = $jsapi.random(words.length) - 1
    return words[wordId].value.word;
}