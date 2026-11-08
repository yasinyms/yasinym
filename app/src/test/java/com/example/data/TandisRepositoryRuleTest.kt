package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class TandisRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveAndGetUserRegistration_authenticatedOwner_succeeds() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repository = TandisRepository(firestore)

        val registration = UserRegistration(
            userId = uid,
            fullName = "سارا احمدی",
            phoneNumber = "09123456789",
            city = "تهران و حومه",
            accountType = "provider",
            guildCategory = "املاک و مستغلات",
            businessTitle = "گروه مشاورین تندیس",
            nationalCode = "0012345678",
            bio = "مشاور تخصصی املاک لوکس در شمال تهران",
            registrationStep = 3L,
            isRegistrationComplete = true
        )

        val saveResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.saveUserRegistration(registration)
        }
        assertTrue(saveResult.isSuccess)

        val fetchedResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.getUserRegistration(uid)
        }
        assertTrue(fetchedResult.isSuccess)
        val fetched = fetchedResult.getOrNull()
        assertNotNull(fetched)
        assertEquals("سارا احمدی", fetched?.fullName)
        assertEquals("provider", fetched?.accountType)
        assertEquals(true, fetched?.isRegistrationComplete)
    }

    @Test
    fun getUserRegistration_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = TandisRepository(firestore)
        withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceRepo.saveUserRegistration(
                UserRegistration(
                    userId = aliceUid,
                    fullName = "سارا احمدی",
                    phoneNumber = "09123456789",
                    city = "تهران و حومه",
                    accountType = "customer",
                    registrationStep = 3L,
                    isRegistrationComplete = true
                )
            ).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        val bobRepo = TandisRepository(firestore)
        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            bobRepo.getUserRegistration(aliceUid)
        }
        assertTrue(result.isFailure)
        val firestoreEx = result.exceptionOrNull()?.unwrapFirestoreException()
        assertNotNull(firestoreEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }

    @Test
    fun createAndObserveListings_authenticatedOwner_emitsCreatedListing() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repository = TandisRepository(firestore)

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createListing(
                UserListing(
                    userId = uid,
                    categoryId = "real_estate",
                    categoryTitle = "املاک",
                    title = "واحد ۱۱۰ متری سعادت‌آباد",
                    price = "۱۰.۵ میلیارد تومان",
                    location = "تهران، سعادت‌آباد",
                    description = "خوش‌نقشه و نورگیر عالی",
                    sellerName = "سارا احمدی",
                    phone = "09123456789"
                )
            )
        }
        assertTrue(createResult.isSuccess)
        val listingId = createResult.getOrThrow()

        val emitted = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeUserListings(uid).first { list -> list.any { it.id == listingId } }
        }
        assertTrue(emitted.any { it.id == listingId && it.title == "واحد ۱۱۰ متری سعادت‌آباد" })
    }

    @Test
    fun observeUserListings_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = TandisRepository(firestore)

        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeUserListings("some_user_123").first()
            }
            fail("Expected exception for unauthenticated user")
        } catch (e: Exception) {
            val firestoreException = checkNotNull(e.unwrapFirestoreException()) {
                "Expected nested FirebaseFirestoreException, got $e"
            }
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException.code)
        }
    }

    private companion object {
        const val ALICE_EMAIL = "alice@tandis.ir"
        const val BOB_EMAIL = "bob@tandis.ir"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
