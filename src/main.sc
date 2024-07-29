require: hangmanGame.js

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
    var = $hangmanGameData

theme: /
    
    state: Start
        q!: $regex</start>
        script:
            $session = {}
            $client = {}
            $temp = {}
            $response = {}
        a: Привет! Предлагаю сыграть в игру "Виселица". Ты называешь букву или слово, а я тебе говорю, прав ты или нет. Помни, чтобы отгадать слово, у тебя есть всего 6 попыток! Сыграем?

    state: LetsPlay
        intent!: /Играем
        a: Давай играть!\nЕсли ты устанешь, то можешь просто написать: "Стоп" и игра прекратится
        go!: /PrintRandWord

    state: PrintRandWord
        script: 
            initSession($session, $hangmanGameData)
            $reactions.answer($session.answer)

    state: StopGame
        intent!: /Стоп
        script: $session.startGame = false
        a: Хорошо, останавливаем игру, твое слово было: {{$session.word}}. Если ты захочешь сыграть еще раз, просто отправь мне сообщение: играть
        
    state: AcceptFile
        event!: fileEvent
        a: Это что, кружок или видео?! Оно будет доступно по ссылке:
        script:
            $request.data.eventData.forEach(function (file) { $reactions.answer(file.url); });

    state: NoMatch
        event!: noMatch
        script:
            if ($session.startGame) {
                tryToGuess($session, $request.query)
            } else {
                $session.answer = "Извините, я вас не понял. Если хотите сыграть в игру, напишите: играть"
            }
            $reactions.answer($session.answer)