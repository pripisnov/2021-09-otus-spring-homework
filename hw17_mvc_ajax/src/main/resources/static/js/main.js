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
            userName: ''
        }
    },
    template:
        '<div>' +
            '<label>add comment:</label>' +
            '<input type="text" placeholder="Title" v-model="title" />' +
            '<input type="text" placeholder="Comment text" v-model="body">' +
            '<input type="text" placeholder="User name" v-model="userName">' +
            '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var comment = {
                bookId: this.book.bookId,
                title: this.title,
                body: this.body,
                userName: this.userName
            };

            commentApi.save({}, comment).then(result =>
                result.json().then(data => {
                    this.comments.push(data);
                    this.title = ''
                    this.body = ''
                    this.userName = ''
                    this.bookId = ''
                })
            )
        }
    }
});

Vue.component('comment-row', {
    props: ['comment', 'comments'],
    template: '<div style="padding-top: 5px">' +
        '| <i>{{ comment.id }}</i> | <b>"{{ comment.title }}"</b> {{ comment.body }} {{ comment.userName }}' +
        '<span style="position: absolute; right: 0">' +
            '<input type="button" value="Delete" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        del: function() {
            commentApi.remove({id: this.comment.id}).then(result => {
                if (result.ok) {
                    this.comments.splice(this.comments.indexOf(this.comment), 1)
                }
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
            genreName: ''
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
                bookApi.update({}, book).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.books, data.bookId);
                        this.books.splice(index, 1, data);
                        this.bookName = ''
                        this.authorName = ''
                        this.genreName = ''
                        this.bookId = ''
                    })
                )
            } else {
                bookApi.save({}, book).then(result =>
                    result.json().then(data => {
                        this.books.push(data);
                        this.bookName = ''
                        this.authorName = ''
                        this.genreName = ''
                    })
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
            showComments: false
        }
    },
    template: '<div style="padding-top: 5px">' +
        '| <i>{{ book.bookId }}</i> | <b>"{{ book.bookName }}"</b> {{ book.authorName }} <i>({{ book.genreName }})</i>' +
        '<span style="position: absolute; right: 0">' +
            '<input type="button" value="Edit" @click="edit" />' +
            '<input type="button" value="Delete" @click="del" />' +
            '<input type="button" value="..." @click="getComments" />' +
        '</span>' +
        '<div v-if="showComments" style="padding-left: 50px; padding-top: 10px;">' +
            '<comment-form :comments="comments" :book="book" />' +
            '<comment-row v-for="comment in comments" :key="comment.id" :comment="comment" :comments="comments"/>' +
        '</div>' +
        '</div>',
    methods: {
        edit: function() {
            this.editMethod(this.book);
        },
        del: function() {
            bookApi.remove({id: this.book.bookId}).then(result => {
                if (result.ok) {
                    this.books.splice(this.books.indexOf(this.book), 1)
                }
            })
        },
        getComments: function () {
            if (!this.showComments && this.comments.length === 0) {
                commentApi.get({id: this.book.bookId}).then(result =>
                    result.json().then(data =>
                        data.forEach(comment => this.comments.push(comment))
                    )
                )
            }

            this.showComments = !this.showComments;
        }
    }
});

Vue.component('book-list', {
    props: ['books'],
    data: function() {
        return {
            book: null
        }
    },
    template:
        '<div style="position: relative; width: 700px;">' +
            '<book-form :books="books" :bookAttr="book" />' +
            '<h4 style="padding-top: 20px;">Book list:</h4>' +
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
            '<book-list :books="books" />' +
        '</div>',
    data: {
        books: []
    },
    created: function() {
        bookApi.get().then(result =>
            result.json().then(data =>
                data.forEach(book => this.books.push(book))
            )
        )
    }
})