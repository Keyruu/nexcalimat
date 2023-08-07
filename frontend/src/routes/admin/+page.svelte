<script lang="ts">
	import { account, handleMyAccount, oidcUser } from '$lib/stores/userManager';
	import { getInitials } from '$lib/utils/accountUtils';
	import { numberCentToEuro } from '$lib/utils/formatEuro';
	import { getAccountPicture, uploadMyAccountImage } from '$lib/utils/pictureUtils';
	import Icon from '@iconify/svelte';
	import { Avatar, FileButton } from '@skeletonlabs/skeleton';
	import { _ } from 'svelte-i18n';

	async function upload(e: Event) {
		console.log('file data:', e);
		const target = e.target as HTMLInputElement;
		if (target.files && target.files[0] && $account) {
			await uploadMyAccountImage(target.files[0]);

			handleMyAccount();
		}
	}
</script>

{#if $oidcUser && $account}
	<div class="flex flex-col justify-center items-center">
		<div class="relative my-4">
			<Avatar
				src="{getAccountPicture($account)}"
				initials="{getInitials($account)}"
				class="w-32 border-4 shadow-md border-surface-300-600-token"
			/>
			<FileButton
				name="files"
				button="btn-icon variant-filled"
				class="bottom-0 right-0 absolute rounded-full border-2 border-slate-400"
				on:change="{upload}"
			>
				<Icon icon="fa6-solid:upload" class="" />
			</FileButton>
		</div>
		<h1 class="mb-4">{$account.name}</h1>
		<div class="mb-4 flex flex-row">
			<p class="font-bold text-xl">{$_('admin.balance')}:&nbsp;</p>
			<p class="text-xl">{numberCentToEuro($account.balance)}</p>
		</div>
		<div class="mb-4 flex flex-row">
			<p class="font-bold text-xl">{$_('admin.email')}:&nbsp;</p>
			<p class="text-xl">{$account.email}</p>
		</div>
	</div>
{:else}
	<h1>{$_('admin.logging-in')}</h1>
{/if}
