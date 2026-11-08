const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write user registration", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user can create and read their own registration", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      fullName: "علی محمدی",
      phoneNumber: "09123456789",
      city: "تهران و حومه",
      accountType: "provider",
      guildCategory: "املاک و مستغلات",
      businessTitle: "مشاورین املاک تندیس",
      nationalCode: "0012345678",
      bio: "متخصص خرید و فروش املاک مسکونی و تجاری",
      registrationStep: 3,
      isRegistrationComplete: true,
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user cannot read or write another user's registration", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    const now = new Date();
    await context.firestore().collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      fullName: "رضا کریمی",
      phoneNumber: "09121112233",
      city: "اصفهان",
      accountType: "customer",
      guildCategory: "",
      businessTitle: "",
      nationalCode: "",
      bio: "",
      registrationStep: 3,
      isRegistrationComplete: true,
      createdAt: now,
      updatedAt: now,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
});

test("Rejects shadow field injection on registration create", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      fullName: "علی محمدی",
      phoneNumber: "09123456789",
      city: "تهران و حومه",
      accountType: "customer",
      guildCategory: "",
      businessTitle: "",
      nationalCode: "",
      bio: "",
      registrationStep: 3,
      isRegistrationComplete: true,
      isAdmin: true,
      createdAt: now,
      updatedAt: now,
    })
  );
});

test("Authenticated user can create and list their own listings", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("listings").doc("listing_1").set({
      userId: ALICE_UID,
      categoryId: "real_estate",
      categoryTitle: "املاک",
      title: "آپارتمان ۱۲۰ متری نوساز",
      price: "۱۲ میلیارد تومان",
      location: "تهران، سعادت‌آباد",
      description: "فول امکانات، دو پارکینگ سندی",
      sellerName: "علی محمدی",
      phone: "09123456789",
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("listings").get()
  );
});
