package com.example.shoppingmall.like;

/*
@SpringBootTest()
class ConcurrencyTest {

	@Autowired
	private ItemLikeService itemLikeService;
	@Autowired
	private ItemLikeRepository itemLikeRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	void setup() {
		user = new User("test", "test@mail.com", UserRole.USER, "asdasd");
		userRepository.save(user);
		Item item = new Item(null, "testName", "testContent", 12345, Category.Beauty, 0L);
		itemRepository.save(item);
		for (int i = 0; i < 100; i++) {
			itemLikeRepository.save(new ItemLike(item, user));
		}
		item.increaseLikeCount(100L);
		itemRepository.save(item);
	}

	@Test
	void conTest() {
		CustomUserDetails userDetails = new CustomUserDetails(1L, "ADMIN", "test@mail.com");
		IntStream.rangeClosed(1, 100).parallel().forEach(i -> itemLikeService.deleteLikeOnItem((long)i, userDetails));
	}
}

*/
