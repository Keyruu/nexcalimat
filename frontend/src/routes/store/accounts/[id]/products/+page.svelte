<script lang="ts">
	import { Swiper } from 'swiper';

	// Import Swiper styles
	import 'swiper/css';

	import 'swiper/css/pagination';

	// import required modules
	import { Pagination } from 'swiper';
	import Product from '$lib/components/products/Product.svelte';
</script>

<div class="products-grid">
	<h1>Hot Drinks</h1>
	<!-- <Swiper
		slidesPerView="auto"
		spaceBetween="{10}"
		centeredSlides="{false}"
		pagination="{{
			clickable: true
		}}"
		modules="{[Pagination]}"
		class="mySwiper"
	>
		{#each { length: 9 } as _, i}
			<Product seed="hott{i}" />
		{/each}
	</Swiper>
	<h1>Cold Drinks</h1>
	<Swiper
		slidesPerView="{7}"
		spaceBetween="{10}"
		centeredSlides="{false}"
		pagination="{{
			clickable: true
		}}"
		modules="{[Pagination]}"
		class="mySwiper"
	>
		{#each { length: 9 } as _, i}
			<Product seed="cold{i}" />
		{/each}
	</Swiper>
	<h1>Favorites</h1>
	<Swiper
		slidesPerView="{7}"
		spaceBetween="{10}"
		centeredSlides="{false}"
		pagination="{{
			clickable: true
		}}"
		modules="{[Pagination]}"
		class="mySwiper"
	>
		{#each { length: 9 } as _, i}
			<Product seed="fave{i}" />
		{/each}
	</Swiper> -->
</div>

<style lang="scss">
	.products-grid {
		grid-area: 2 / 1 / 6 / 6;
	}
</style>
