<script lang="ts">
	import { AlertType } from '$lib/types/AlertType';
	import Icon from '@iconify/svelte';

	const icons: Record<AlertType, { icon: string; styleClass: string }> = {
		[AlertType.Info]: { icon: 'mdi:information-outline', styleClass: 'alert-info' },
		[AlertType.Success]: { icon: 'mdi:check-circle-outline', styleClass: 'alert-success' },
		[AlertType.Warning]: { icon: 'mdi:alert-circle-outline', styleClass: 'alert-warning' },
		[AlertType.Error]: { icon: 'mdi:close-outline-circle', styleClass: 'alert-error' }
	};

	export let type: AlertType = AlertType.Info;

	$: icon = icons[type].icon;
	$: styleClass = icons[type].styleClass;

	$: {
		console.log('type changed', type);
		console.log('icons', icons[type].icon);
	}
</script>

<div class="p-4">
	<div class="{`alert shadow-lg ${styleClass}`}">
		<div>
			<Icon icon="{icon}" style="font-size: 24px" />
			<span><slot /></span>
		</div>
	</div>
</div>
