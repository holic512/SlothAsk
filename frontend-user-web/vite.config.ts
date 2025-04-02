import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";
import { visualizer } from "rollup-plugin-visualizer";
import path from 'path';

export default defineConfig({
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // 确保这里的路径与 tsconfig 中一致
    }
  },
  plugins: [
    vue(),
    vueJsx(),
    visualizer({ open: true }) // 打包分析工具
  ],
  build: {
    sourcemap: false, // 关闭 sourcemap 提高构建速度
    minify: "esbuild", // esbuild 压缩更快
    chunkSizeWarningLimit: 500, // 降低 chunk 体积警告
  },
  optimizeDeps: {
    include: ["vue", "vue-router", "pinia"], // 预编译依赖
  },
  define: {
    __VUE_OPTIONS_API__: false, // 关闭 Vue Options API
    __VUE_PROD_DEVTOOLS__: false, // 禁用 Vue DevTools
  },
  server: {
    port: 3000, // 指定端口
    open: true, // 自动打开浏览器
  },
});
