package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final DevLibraryAccessors laccForDevLibraryAccessors = new DevLibraryAccessors(owner);
    private final IoLibraryAccessors laccForIoLibraryAccessors = new IoLibraryAccessors(owner);
    private final NetLibraryAccessors laccForNetLibraryAccessors = new NetLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>dev</b>
     */
    public DevLibraryAccessors getDev() {
        return laccForDevLibraryAccessors;
    }

    /**
     * Group of libraries at <b>io</b>
     */
    public IoLibraryAccessors getIo() {
        return laccForIoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>net</b>
     */
    public NetLibraryAccessors getNet() {
        return laccForNetLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class DevLibraryAccessors extends SubDependencyFactory {
        private final DevArbjergLibraryAccessors laccForDevArbjergLibraryAccessors = new DevArbjergLibraryAccessors(owner);

        public DevLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>dev.arbjerg</b>
         */
        public DevArbjergLibraryAccessors getArbjerg() {
            return laccForDevArbjergLibraryAccessors;
        }

    }

    public static class DevArbjergLibraryAccessors extends SubDependencyFactory {

        public DevArbjergLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>lavaplayer</b> with <b>dev.arbjerg:lavaplayer</b> coordinates and
         * with version reference <b>dev.arbjerg.lavaplayer</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLavaplayer() {
            return create("dev.arbjerg.lavaplayer");
        }

    }

    public static class IoLibraryAccessors extends SubDependencyFactory {
        private final IoGithubLibraryAccessors laccForIoGithubLibraryAccessors = new IoGithubLibraryAccessors(owner);

        public IoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.github</b>
         */
        public IoGithubLibraryAccessors getGithub() {
            return laccForIoGithubLibraryAccessors;
        }

    }

    public static class IoGithubLibraryAccessors extends SubDependencyFactory {
        private final IoGithubCdimascioLibraryAccessors laccForIoGithubCdimascioLibraryAccessors = new IoGithubCdimascioLibraryAccessors(owner);

        public IoGithubLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.github.cdimascio</b>
         */
        public IoGithubCdimascioLibraryAccessors getCdimascio() {
            return laccForIoGithubCdimascioLibraryAccessors;
        }

    }

    public static class IoGithubCdimascioLibraryAccessors extends SubDependencyFactory {
        private final IoGithubCdimascioDotenvLibraryAccessors laccForIoGithubCdimascioDotenvLibraryAccessors = new IoGithubCdimascioDotenvLibraryAccessors(owner);

        public IoGithubCdimascioLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.github.cdimascio.dotenv</b>
         */
        public IoGithubCdimascioDotenvLibraryAccessors getDotenv() {
            return laccForIoGithubCdimascioDotenvLibraryAccessors;
        }

    }

    public static class IoGithubCdimascioDotenvLibraryAccessors extends SubDependencyFactory {

        public IoGithubCdimascioDotenvLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>java</b> with <b>io.github.cdimascio:dotenv-java</b> coordinates and
         * with version reference <b>io.github.cdimascio.dotenv.java</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJava() {
            return create("io.github.cdimascio.dotenv.java");
        }

    }

    public static class NetLibraryAccessors extends SubDependencyFactory {
        private final NetDv8tionLibraryAccessors laccForNetDv8tionLibraryAccessors = new NetDv8tionLibraryAccessors(owner);

        public NetLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>net.dv8tion</b>
         */
        public NetDv8tionLibraryAccessors getDv8tion() {
            return laccForNetDv8tionLibraryAccessors;
        }

    }

    public static class NetDv8tionLibraryAccessors extends SubDependencyFactory {

        public NetDv8tionLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jda</b> with <b>net.dv8tion:JDA</b> coordinates and
         * with version reference <b>net.dv8tion.jda</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJda() {
            return create("net.dv8tion.jda");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final DevVersionAccessors vaccForDevVersionAccessors = new DevVersionAccessors(providers, config);
        private final IoVersionAccessors vaccForIoVersionAccessors = new IoVersionAccessors(providers, config);
        private final NetVersionAccessors vaccForNetVersionAccessors = new NetVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.dev</b>
         */
        public DevVersionAccessors getDev() {
            return vaccForDevVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.io</b>
         */
        public IoVersionAccessors getIo() {
            return vaccForIoVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.net</b>
         */
        public NetVersionAccessors getNet() {
            return vaccForNetVersionAccessors;
        }

    }

    public static class DevVersionAccessors extends VersionFactory  {

        private final DevArbjergVersionAccessors vaccForDevArbjergVersionAccessors = new DevArbjergVersionAccessors(providers, config);
        public DevVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.dev.arbjerg</b>
         */
        public DevArbjergVersionAccessors getArbjerg() {
            return vaccForDevArbjergVersionAccessors;
        }

    }

    public static class DevArbjergVersionAccessors extends VersionFactory  {

        public DevArbjergVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>dev.arbjerg.lavaplayer</b> with value <b>2.2.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLavaplayer() { return getVersion("dev.arbjerg.lavaplayer"); }

    }

    public static class IoVersionAccessors extends VersionFactory  {

        private final IoGithubVersionAccessors vaccForIoGithubVersionAccessors = new IoGithubVersionAccessors(providers, config);
        public IoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.github</b>
         */
        public IoGithubVersionAccessors getGithub() {
            return vaccForIoGithubVersionAccessors;
        }

    }

    public static class IoGithubVersionAccessors extends VersionFactory  {

        private final IoGithubCdimascioVersionAccessors vaccForIoGithubCdimascioVersionAccessors = new IoGithubCdimascioVersionAccessors(providers, config);
        public IoGithubVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.github.cdimascio</b>
         */
        public IoGithubCdimascioVersionAccessors getCdimascio() {
            return vaccForIoGithubCdimascioVersionAccessors;
        }

    }

    public static class IoGithubCdimascioVersionAccessors extends VersionFactory  {

        private final IoGithubCdimascioDotenvVersionAccessors vaccForIoGithubCdimascioDotenvVersionAccessors = new IoGithubCdimascioDotenvVersionAccessors(providers, config);
        public IoGithubCdimascioVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.github.cdimascio.dotenv</b>
         */
        public IoGithubCdimascioDotenvVersionAccessors getDotenv() {
            return vaccForIoGithubCdimascioDotenvVersionAccessors;
        }

    }

    public static class IoGithubCdimascioDotenvVersionAccessors extends VersionFactory  {

        public IoGithubCdimascioDotenvVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>io.github.cdimascio.dotenv.java</b> with value <b>3.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJava() { return getVersion("io.github.cdimascio.dotenv.java"); }

    }

    public static class NetVersionAccessors extends VersionFactory  {

        private final NetDv8tionVersionAccessors vaccForNetDv8tionVersionAccessors = new NetDv8tionVersionAccessors(providers, config);
        public NetVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.net.dv8tion</b>
         */
        public NetDv8tionVersionAccessors getDv8tion() {
            return vaccForNetDv8tionVersionAccessors;
        }

    }

    public static class NetDv8tionVersionAccessors extends VersionFactory  {

        public NetDv8tionVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>net.dv8tion.jda</b> with value <b>5.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJda() { return getVersion("net.dv8tion.jda"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
