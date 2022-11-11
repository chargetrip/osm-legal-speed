/* eslint-disable @typescript-eslint/require-await */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
const { prepare } = require('@semantic-release/changelog');

async function chargetripChangelog(pluginConfig, context) {
  if (context.branch.type !== 'release') {
    pluginConfig.changelogFile = `docs/changelog/${context.nextRelease.version}.md`;
  }

  return prepare(pluginConfig, context);
}

module.exports = { prepare: chargetripChangelog };