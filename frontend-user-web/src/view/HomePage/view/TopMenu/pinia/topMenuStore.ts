import {defineStore} from 'pinia';
import {ref} from 'vue';

export const useTopMenuStore = defineStore('TopMenuStore', () => {
    const isLogin = ref<boolean>(false);

    return {
        isLogin
    };
});


