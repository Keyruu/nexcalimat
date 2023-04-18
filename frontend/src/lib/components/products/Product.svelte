<script lang="ts">
	export let seed: string;
</script>

<div class="m-4">
	<div class="card w-64 h-72 bg-base-100 shadow-xl">
		<figure><img src="https://picsum.photos/seed/{seed}/400/400" alt="Shoes" /></figure>
		<div class="card-body">
			<h2 class="card-title">{seed}</h2>
			<div class="card-actions justify-end">
				<button class="btn btn-primary">Buy Now</button>
			</div>
		</div>
	</div>
</div>
