#!/usr/bin/env node

(async () => {
  const { readFileSync, writeFileSync } = require("fs"),
    argv = process.argv.filter(
      (arg) => !arg.includes("node") && !arg.includes("version.js")
    ),
    data = readFileSync("./pom.xml", "utf8");

  let index = 0;
  const newData = data.replace(/<version>(.*)<\/version>/g, (value) =>
    index++ === 0 ? `<version>${argv[0]}</version>` : value
  );

  if (argv.length < 1) {
    throw new Error("Version not provided");
  }

  writeFileSync("./pom.xml", newData, "utf8");
})().catch(console.error);