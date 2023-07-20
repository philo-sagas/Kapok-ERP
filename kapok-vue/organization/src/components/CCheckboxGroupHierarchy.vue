<template>
  <slot></slot>
  <template v-for="item in groups" :key="item.value">
    <v-row no-gutters align="center" :style="rowStyle">
      <v-col cols="12" md="12">
        <v-checkbox
          :label="item.title"
          :value="item.value"
          :indeterminate="isIndeterminateGroupCheckbox(item)"
          :model-value="props.modelValue"
          @update:modelValue="onUpdateModelValue"
          @change="onChangeGroupCheckbox"
          color="info"
          hide-details
        ></v-checkbox>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <c-checkbox-group-hierarchy
      :model-value="props.modelValue"
      @update:modelValue="onUpdateModelValue"
      :parent="item"
      :items="item.children"
      :level="props.level + 1">
    </c-checkbox-group-hierarchy>
  </template>
  <template v-if="leaves">
    <v-row no-gutters align="center" :style="rowStyle">
      <v-col v-for="item in leaves" :key="item.value" cols="12" md="2">
        <v-checkbox
          :label="item.title"
          :value="item.value"
          :model-value="props.modelValue"
          @update:modelValue="onUpdateModelValue"
          color="info"
          hide-details
        ></v-checkbox>
      </v-col>
    </v-row>
    <v-divider></v-divider>
  </template>
</template>

<script setup>
import {computed} from 'vue'

defineOptions({
  inheritAttrs: false
})
const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  items: {
    type: Array,
    default: () => []
  },
  parent: {
    type: Object,
    default: null
  },
  level: {
    type: Number,
    default: 0
  }
})
const emit = defineEmits(['update:modelValue', 'change:leafValue'])

const groups = computed(() => props.items.filter(i => i.children))
const leaves = computed(() => props.items.filter(i => !i.children))
const rowStyle = computed(() => ({paddingLeft: props.level * 32 + 'px'}))

function isIndeterminateGroupCheckbox(item) {
  const one = item.children.find(i => props.modelValue.includes(i.value))
  const all = item.children.every(i => props.modelValue.includes(i.value))
  return one != null && !all
}

function onUpdateModelValue(value) {
  console.info('onUpdateModelValue: ' + value)
  if (props.parent) {
    const one = props.items.find(i => value.includes(i.value))
    if (one != null) {
      emit('update:modelValue', Array.from(new Set([...value, props.parent.value])))
    } else {
      emit('update:modelValue', value.filter(x => x !== props.parent.value))
    }
  } else {
    emit('update:modelValue', value)
  }
}

function onChangeGroupCheckbox(event) {
  const value = event.target._value
  const valueSet = new Set()
  const root = props.items.find(i => i.value === value)
  const queue = [...root.children]
  while (queue.length) {
    const item = queue.pop()
    valueSet.add(item.value)
    if (item.children) {
      queue.push(...item.children)
    }
  }
  if (event.target.checked) {
    emit('update:modelValue', Array.from(new Set([...props.modelValue, ...valueSet])))
  } else {
    emit('update:modelValue', props.modelValue.filter(x => !valueSet.has(x)))
  }
}
</script>
