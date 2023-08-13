<script lang="ts">
	import { goto } from '$app/navigation';
	import { base } from '$app/paths';
	import {
		CreateProductDocument,
		ProductType,
		UpdateProductDocument,
		type CreateProductMutation,
		type CreateProductMutationVariables,
		type Product,
		type UpdateProductMutation,
		type UpdateProductMutationVariables
	} from '$lib/generated/graphql';
	import { deleteProductImage, getProductPicture, uploadProductImage } from '$lib/utils/pictureUtils';
	import { handleError } from '$lib/utils/storeError';
	import { toastError, toastSuccess } from '$lib/utils/toastUtils';
	import Icon from '@iconify/svelte';
	import { FileDropzone, toastStore } from '@skeletonlabs/skeleton';
	import { mutationStore } from '@urql/svelte';
	import { onDestroy } from 'svelte';
	import { _ } from 'svelte-i18n';
	import type { Unsubscriber } from 'svelte/store';
	import { client } from '../../../urqlClient';

	export let productBind: Product = {};
	export let editMode = true;

	const updateProduct = (product: Product) =>
		mutationStore<UpdateProductMutation, UpdateProductMutationVariables>({
			client,
			query: UpdateProductDocument,
			variables: {
				product
			}
		});

	const createProduct = (product: Product) =>
		mutationStore<CreateProductMutation, CreateProductMutationVariables>({
			client,
			query: CreateProductDocument,
			variables: {
				product
			}
		});

	let updateProductUnsubscribe: Unsubscriber;
	let createProductUnsubscribe: Unsubscriber;

	let newImageFile: File | undefined;
	let newImage: HTMLImageElement = new Image();
	let deleteOldImage = false;

	async function setFile(e: Event) {
		const target = e.target as HTMLInputElement;
		if (target.files && target.files.length > 0) {
			newImageFile = target.files![0];
			const reader = new FileReader();
			reader.onload = (e) => {
				newImage.src = e.target!.result as string;
				newImage.onload = () => {
					if (newImage.width > 2000 || newImage.height > 2000) {
						toastStore.trigger(toastError($_('admin.image.too-large')));
						newImageFile = undefined;
						newImage = new Image();
					}
				};
			};
			reader.readAsDataURL(newImageFile);
		}
	}

	function saveProduct() {
		if (productBind.price) {
			if (productBind.type === ProductType.HotDrink) {
				productBind.bundleSize = 0;
			}
			if (editMode) {
				updateProductUnsubscribe = updateProduct({
					...productBind,
					price: Math.round(productBind.price * 100)
				}).subscribe(async (result) => {
					if (result.data?.updateProduct?.id) {
						await imageUpload(result.data.updateProduct);
					} else if (result.error) {
						handleError(result.error);
					}
				});
			} else {
				createProductUnsubscribe = createProduct({
					...productBind,
					price: Math.round(productBind.price * 100)
				}).subscribe(async (result) => {
					if (result.data?.createProduct?.id) {
						await imageUpload(result.data.createProduct);
					} else if (result.error) {
						handleError(result.error);
					}
				});
			}
		}
	}

	async function imageUpload(product: Product) {
		if (newImageFile) {
			const res = await uploadProductImage(newImageFile, product);
			if (res.status === 201) {
				productSaved();
			} else {
				toastStore.trigger(toastError($_('admin.image.upload-failed')));
			}
		} else if (deleteOldImage) {
			const res = await deleteProductImage(product);
			if (res.status === 204) {
				productSaved();
			} else {
				toastStore.trigger(toastError($_('admin.image.upload-failed')));
			}
		} else {
			productSaved();
		}
	}

	function productSaved() {
		toastStore.trigger(toastSuccess($_('admin.product-form.save')));
		goto('/admin/products');
	}

	function clearNewImage() {
		productBind.picture = undefined;
		newImageFile = undefined;
	}

	onDestroy(() => {
		updateProductUnsubscribe?.();
		createProductUnsubscribe?.();
	});
</script>

<form on:submit="{saveProduct}" class="flex justify-center items-center flex-col">
	{#if productBind}
		<h2 class="my-4">{editMode ? $_('admin.product-form.edit-title') : $_('admin.product-form.add-title')}</h2>

		<div class="my-2 flex flex-row">
			<div class="flex-shrink-0 relative">
				{#if productBind.picture}
					<button
						on:click="{() => (deleteOldImage = !deleteOldImage)}"
						type="button"
						class="absolute btn-icon variant-filled-surface -top-3 -left-3"
						><Icon icon="{deleteOldImage ? 'fa-solid:undo-alt' : 'fa6-solid:xmark'}" /></button
					>
				{/if}
				<img
					class=" h-40 border-surface-500 border-2 mr-4"
					src="{deleteOldImage
						? `${base}/img/default_bottle.png`
						: newImageFile
						? newImage.src
						: getProductPicture(productBind)}"
					alt="Product"
				/>
			</div>
			<FileDropzone name="files" on:change="{setFile}">
				<svelte:fragment slot="message">{$_('admin.product-form.file-dropzone.message')}</svelte:fragment>
				<svelte:fragment slot="meta">{$_('admin.product-form.file-dropzone.meta')}</svelte:fragment>
			</FileDropzone>
		</div>
		{#if newImageFile}
			<p>{$_('admin.product-form.new-image')}: {newImageFile.name}</p>
			<button on:click="{clearNewImage}" class="btn variant-filled-surface">Clear</button>
		{/if}
		<hr class="w-[30rem] my-4" />

		<div class="w-72">
			<label class="label my-2">
				<span>{$_('admin.product-form.name')}</span>
				<input required class="input" type="text" placeholder="Spezi" bind:value="{productBind['name']}" />
			</label>

			<label class="label my-2">
				<span>{$_('admin.product-form.type')}</span>
				<select class="select" required bind:value="{productBind['type']}">
					<option value="{ProductType.HotDrink}">{$_('admin.product-form.hot-drink')}</option>
					<option value="{ProductType.ColdDrink}">{$_('admin.product-form.cold-drink')}</option>
				</select>
			</label>

			<label class="label my-2">
				<span>{$_('admin.product-form.price')}</span>
				<div class="input-group input-group-divider grid-cols-[auto_1fr_auto]">
					<input
						type="number"
						placeholder="{$_('admin.product-form.price-placeholder')}"
						step=".01"
						min="0"
						bind:value="{productBind['price']}"
					/>
					<div class="input-group-shim"><Icon icon="fa6-solid:euro-sign" /></div>
				</div>
			</label>

			{#if productBind.type === ProductType.ColdDrink}
				<label class="label my-2">
					<span>{$_('admin.product-form.bundle-size')}</span>
					<input
						class="input"
						type="number"
						min="0"
						max="30"
						placeholder="12"
						bind:value="{productBind['bundleSize']}"
					/>
				</label>
			{/if}
			<div class="flex my-2">
				<a href="/admin/products" class="btn variant-filled-error">{$_('cancel')}</a>
				<button class="btn variant-filled-success ml-auto" type="submit">{$_('save')}</button>
			</div>
		</div>
	{/if}
</form>
