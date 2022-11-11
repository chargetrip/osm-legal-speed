/* eslint-disable @typescript-eslint/require-await */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
const { prepare } = require('@semantic-release/git');

async function chargetripChangelog(pluginConfig, context) {
  if (context.branch.type !== 'release') {
    pluginConfig.assets = pluginConfig.assets.filter(
      file => file !== 'CHANGELOG.md',
    );
  }

  return prepare(pluginConfig, context);
}

module.exports = { prepare: chargetripChangelog };