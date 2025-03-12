function getHint(question, answer){
    $.ajax({
        url: '/getHint',
        type: 'GET',
        data: { question: question, answerQuestion: answer },
        success: function(response) {
            $('#hintResult').text(response);
        },
        error: function() {
            $('#hintResult').text('Error retrieving hint.');
        }
    });

}