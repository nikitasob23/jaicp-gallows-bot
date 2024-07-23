require: wordHandler.js

require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: text/text.sc
    module = zenbot-common
    
require: where/where.sc
    module = zenbot-common

require: common.js
    module = zenbot-common

require: hangmanGameData.csv
    name = HangmanGameData
    var = $HangmanGameData

theme: /

    # state: Start
    #     q!: $regex</start>
    #     a: Привет! Предлагаю сыграть в игру "Виселица". Ты называешь букву или слово, а я тебе говорю, прав ты или нет. Помни, чтобы отгадать слово, у тебя есть всего 6 попыток! Сыграем?
    
    # state: /LetsPlay
    # intent: /играем
    # a: Давай!
    
    state: Start
        q!: $regex</start>
        script:
            $session = {}
            $client = {}
            $temp = {}
            $response = {}
        a: Привет! Предлагаю сыграть в игру "Виселица". Ты называешь букву или слово, а я тебе говорю, прав ты или нет. Помни, чтобы отгадать слово, у тебя есть всего 6 попыток! Сыграем?

    state: Hello
        intent!: /играем
        a: Давай играть!
        go!: /PrintRandWord

    state: PrintRandWord
        script:
            $session.word = chooseRandWord($HangmanGameData)
            $session.mask = returnMask($session.word)
            $attemptsToGuess = 6
            # 
            $reactions.answer("Ваше слово: " + $session.word)
            # 
            $reactions.answer("Ваше слово: " + printMask($session.mask))

    state: NoMatch
        event!: noMatch
        script:
            if ($request.query.length == 1) {
                var i = 0
                while (i < $session.word.length) {
                    if ($session.word[i] == $request.query) {
                        $session.mask[i] = $request.query
                    }
                    i++
                }
            } else {
                if ($request.query == $session.word) {
                    $reactions.answer("Поздравяю, вы выиграли! Загаданное слово: " + printMask($session.mask))
                } else {
                    $reactions.answer("Увы, вы не угадали...")
                }
            }
            
            $attemptsToGuess--
            
            if ($attemptsToGuess == 0) {
                $reactions.answer("Вы проиграли, все попытки изчерпаны. Загаданное слово: " + $session.word)
            } else if ($attemptsToGuess == 2) {
                $reactions.answer("Осторожнее! У вас осталось всего 2 попытки... Ваше слово: " + printMask($session.mask))
            } else {
                $reactions.answer("Ваше слово: " + printMask($session.mask))
            }