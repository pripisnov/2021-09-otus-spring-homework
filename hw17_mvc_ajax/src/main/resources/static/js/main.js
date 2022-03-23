function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].bookId === id) {
            return i;
        }
    }

    return -1;
}

var bookApi = Vue.resource('/books{/id}');
var commentApi = Vue.resource('/comments{/id}');

Vue.component('comment-form', {
    props: ['comments', "book"],
    data: function() {
        return {
            bookId: '',
            title: '',
            body: '',
            userName: '',
            errorMessage: ''
        }
    },
    template:
        '<div>' +
            '<label>add comment:</label>' +
            '<input type="text" placeholder="Title" v-model="title" />' +
            '<input type="text" placeholder="Comment text" v-model="body">' +
            '<input type="text" placeholder="User name" v-model="userName">' +
            '<input type="button" value="Save" @click="save" />' +
        '<div v-if="errorMessage" style="color: red;" > {{errorMessage}}' +
        '</div>' +
        '</div>',
    methods: {
        save: function() {
            this.errorMessage = '';

            var comment = {
                bookId: this.book.bookId,
                title: this.title,
                body: this.body,
                userName: this.userName
            };

            commentApi.save({}, comment).then(result => {
                    if (result.ok) {
                        result.json().then(data => {
                            this.comments.push(data);
                            this.title = ''
                            this.body = ''
                            this.userName = ''
                            this.bookId = ''
                        })
                    }
                }, errorResult => {
                    errorResult.json().then(errorData =>
                        this.errorMessage = errorData.errorMessage
                    )
                }
            )
        }
    }
});

Vue.component('comment-row', {
    props: ['comment', 'comments'],
    data: function () {
        return {
            errorMessage: ''
        }
    },
    template: '<div style="padding-top: 5px">' +
        '<div>| <i>{{ comment.id }}</i> | <b>"{{ comment.title }}"</b></div>' +
        '<div>{{ comment.body }}</div>' +
        '<div><i>@{{ comment.userName }}</i></div>' +
        '<div>' +
            '<input type="button" value="Delete" @click="del" />' +
        '</div>' +
        '<div v-if="errorMessage" style="color: red">{{ errorMessage }}</div>' +
        '</div>',
    methods: {
        del: function() {
            this.errorMessage = '';
            commentApi.remove({id: this.comment.id}).then(result => {
                if (result.ok) {
                    this.comments.splice(this.comments.indexOf(this.comment), 1)
                }
            }, errorResult => {
                errorResult.json().then(errorData =>
                    this.errorMessage = errorData.errorMessage
                )
            })
        }
    }
});

Vue.component('book-form', {
    props: ['books', 'bookAttr'],
    data: function() {
        return {
            bookId: '',
            bookName: '',
            authorName: '',
            genreName: '',
            errorMessage: ''
        }
    },
    watch: {
        bookAttr: function(newVal, oldVal) {
            this.bookName = newVal.bookName;
            this.authorName = newVal.authorName;
            this.genreName = newVal.genreName;
            this.bookId = newVal.bookId;
        }
    },
    template:
        '<div>' +
        '<div>' +
            '<label>Book:</label>' +
            '<input type="text" placeholder="Book name" v-model="bookName" />' +
        '</div>' +
        '<div>' +
            '<label>Author:</label>' +
            '<select v-model="authorName">' +
                '<option value="Дж.Дж. Мартин">Дж.Дж. Мартин</option>' +
                '<option value="А.С. Пушкин">А.С. Пушкин</option>' +
            '</select>' +
        '</div>' +
        '<div>' +
            '<label>Genre:</label>' +
            '<select v-model="genreName">' +
                '<option value="Сказка">Сказка</option>' +
                '<option value="Фэнтези">Фэнтези</option>' +
            '</select>' +
        '</div>' +
        '<div><input type="button" value="Save" @click="save" /></div>' +
        '<div v-if="errorMessage" style="color: red">{{ errorMessage }}</div>' +
        '</div>',
    methods: {
        save: function() {
            var book = {
                bookId: this.bookId,
                bookName: this.bookName,
                authorName: this.authorName,
                genreName: this.genreName
            };

            if (this.bookId) {
                bookApi.update({}, book).then(result => {
                        if (result.ok) {
                            result.json().then(data => {
                                var index = getIndex(this.books, data.bookId);
                                this.books.splice(index, 1, data);
                                this.bookName = ''
                                this.authorName = ''
                                this.genreName = ''
                                this.bookId = ''
                                this.errorMessage = ''
                            })
                        }
                    }, errorResult => {
                        errorResult.json().then(errorData =>
                            this.errorMessage = errorData.errorMessage
                        )
                    }
                )
            } else {
                bookApi.save({}, book).then(result => {
                        if (result.ok) {
                            result.json().then(data => {
                                this.books.push(data);
                                this.bookName = ''
                                this.authorName = ''
                                this.genreName = ''
                                this.errorMessage = ''
                            })
                        }
                    }, errorResult => {
                        errorResult.json().then(errorData =>
                            this.errorMessage = errorData.errorMessage
                        )
                    }
                )
            }
        }
    }
});


Vue.component('book-row', {
    props: ['book', 'editMethod', 'books'],
    data: function () {
        return {
            comments: [],
            showComments: false,
            bookErrorMessage: '',
            commentsErrorMessage: ''
        }
    },
    template: '<div style="padding-top: 5px">' +
        '| <i>{{ book.bookId }}</i> | <b>"{{ book.bookName }}"</b> {{ book.authorName }} <i>({{ book.genreName }})</i>' +
        '<span style="position: absolute; right: 0">' +
            '<input type="button" value="Edit" @click="edit" />' +
            '<input type="button" value="Delete" @click="del" />' +
            '<input type="button" value="..." @click="getComments" />' +
        '</span>' +
        '<div v-if="bookErrorMessage" style="color: red;"> {{ bookErrorMessage }} </div>' +
        '<div v-if="showComments" style="padding-left: 50px; padding-top: 10px;">' +
            '<comment-form :comments="comments" :book="book" />' +
            '<div v-if="commentsErrorMessage" style="color: red;"> {{ commentsErrorMessage }} </div>' +
            '<comment-row v-for="comment in comments" :key="comment.id" :comment="comment" :comments="comments"/>' +
        '</div>' +
        '</div>',
    methods: {
        edit: function() {
            this.clearErrorMessages();
            this.editMethod(this.book);
        },
        del: function() {
            this.clearErrorMessages();
            bookApi.remove({id: this.book.bookId}).then(result => {
                if (result.ok) {
                    this.books.splice(this.books.indexOf(this.book), 1)
                }
            }, errorResult => {
                errorResult.json().then(errorData =>
                    this.bookErrorMessage = errorData.errorMessage
                )
            })
        },
        getComments: function () {
            this.clearErrorMessages();
            if (!this.showComments && this.comments.length === 0) {
                commentApi.get({id: this.book.bookId}).then(result => {
                        if (result.ok) {
                            result.json().then(data =>
                                data.forEach(comment => this.comments.push(comment))
                            )
                        }
                    }, errorResult => {
                        errorResult.json().then(errorData =>
                            this.commentsErrorMessage = errorData.errorMessage
                        )
                    }
                )
            }

            this.showComments = !this.showComments;
        },
        clearErrorMessages: function () {
            this.commentsErrorMessage = ''
            this.bookErrorMessage = ''
        }
    }
});

Vue.component('book-list', {
    props: ['books', 'errorMessage'],
    data: function() {
        return {
            book: null
        }
    },
    template:
        '<div style="position: relative; width: 700px;">' +
            '<book-form :books="books" :bookAttr="book" />' +
            '<h4 style="padding-top: 20px;">' +
                'Book list (Нажать "..." для комментария, повторное нажатие - скрыть комментарии):' +
        '   </h4>' +
            '<div v-if="errorMessage" style="color: red;">{{ errorMessage }}</div>' +
            '<div><book-row v-for="book in books" :key="book.bookId" :book="book" ' +
                ':editMethod="editMethod" :books="books" /></div>' +
        '</div>',
    methods: {
        editMethod: function(book) {
            this.book = book;
        }
    }
});

var app = new Vue({
    el: '#app',
    template:
        '<div>' +
            '<h2>Cool Books:</h2>' +
            '<book-list :books="books" :errorMessage="errorMessage"/>' +
        '</div>',
    data: {
        books: [],
        errorMessage: ''
    },
    created: function() {
        bookApi.get().then(result => {
                if (result.ok) {
                    result.json().then(data =>
                        data.forEach(book => this.books.push(book)))
                }
            }, errorResult => {
                errorResult.json().then(data =>
                    this.errorMessage = data.errorMessage
                )
            }
        )
    }
})