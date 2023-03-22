module.exports = {
  dest: "dist",
  base: `/`,
  locales: {
    "/": {
      lang: "zh",
      title: "JAVA框架组件集-Screw",
      description: "Screw文档系统"
    }
  },
  head: [["link", { rel: "icon", href: `/assets/img/logo.jpeg` }]],
  cache: false,
  port: "80",
  themeConfig: {
    logo: `/assets/img/logo.jpeg`,
    smoothScroll: true,
    displayAllHeaders: true,
    locales: {
      "/": {
        label: "简体中文",
        lastUpdated: "Last Updated",
        nav: require("./nav/zh"),
        sidebar: require("./sidebar/zh")
      }
    }
  },
  plugins: [
    ["@vuepress/back-to-top", true],
    [
      "@vuepress/pwa",
      {
        serviceWorker: true,
        updatePopup: true
      }
    ],
    ["@vuepress/medium-zoom", true],
    [
      "@vuepress/google-analytics",
      {
        ga: "UA-128189152-1"
      }
    ],
    ["flowchart"],
    ["vuepress-plugin-mermaidjs"]
  ]
};
