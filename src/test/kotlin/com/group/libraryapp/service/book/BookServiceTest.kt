package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {
    @AfterEach
    fun claen() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록이 정상 작동한다.")
    fun saveBookTest() {
        // given
        val request = BookRequest("처음 시작하는 FastAPI", BookType.COMPUTER)

        // when
        bookService.saveBook(request)

        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books.first().name).isEqualTo(request.name)
        assertThat(books.first().type).isEqualTo(request.type)
    }

    @Test
    @DisplayName("책 대출이 정상 동작한다.")
    fun loanBookTest() {
        // given
        bookRepository.save(Book.fixture("처음 시작하는 FastAPI"))
        val savedUser = userRepository.save(User("박형주", 22))
        val request = BookLoanRequest("박형주", "처음 시작하는 FastAPI")

        // when
        bookService.loanBook(request)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo(request.bookName)
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].user.name).isEqualTo(savedUser.name)
        assertThat(results[0].user.age).isEqualTo(savedUser.age)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    @DisplayName("책이 진작 대출되어 있다면 신규 대출에 실패한다.")
    fun loanBookFailTest() {
        // given
        bookRepository.save(Book.fixture("처음 시작하는 FastAPI"))
        val savedUser = userRepository.save(User("박형주", 22))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "처음 시작하는 FastAPI"))
        val request = BookLoanRequest("박형주", "처음 시작하는 FastAPI")


        // when & then
        val message = assertThrows <IllegalArgumentException>{
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납이 정상 동작한다.")
    fun returnBook() {
        // given
        bookRepository.save(Book.fixture("처음 시작하는 FastAPI"))
        val savedUser = userRepository.save(User("박형주", 22))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "처음 시작하는 FastAPI"))
        val request = BookReturnRequest("박형주", "처음 시작하는 FastAPI")

        // when
        bookService.returnBook(request)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }
}