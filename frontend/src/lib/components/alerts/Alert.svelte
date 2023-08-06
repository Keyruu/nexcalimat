<script lang="ts">
	import { AlertType } from '$lib/types/AlertType';
	import Icon from '@iconify/svelte';

	const icons: Record<AlertType, { icon: string; styleClass: string }> = {
		[AlertType.Info]: { icon: 'mdi:information-outline', styleClass: 'variant-filled-surface' },
		[AlertType.Success]: { icon: 'mdi:check-circle-outline', styleClass: 'variant-filled-success' },
		[AlertType.Warning]: { icon: 'mdi:alert-circle-outline', styleClass: 'variant-filled-warning' },
		[AlertType.Error]: { icon: 'mdi:close-circle-outline', styleClass: 'variant-filled-error' }
	};

	export let type: AlertType = AlertType.Info;

	$: icon = icons[type].icon;
	$: styleClass = icons[type].styleClass;
</script>

<div class="p-4">
	<aside class="{`alert shadow-lg ${styleClass} flex flex-row items-center`}">
		<Icon class="mr-2" {icon} style="font-size: 24px" />
		<slot />
	</aside>
</div>
