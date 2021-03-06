/**
 * Copyright 2016-2017 The Reaktivity Project
 *
 * The Reaktivity Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.reaktivity.nukleus.http_cache.internal.streams.server;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.reaktivity.reaktor.test.ReaktorRule;

public class ProxyCacheIT
{
    private final K3poRule k3po = new K3poRule()
        .addScriptRoot("route", "org/reaktivity/specification/nukleus/http_cache/control/route")
        .addScriptRoot("streams", "org/reaktivity/specification/nukleus/http_cache/streams/proxy");

    private final TestRule timeout = new DisableOnDebug(new Timeout(15, SECONDS));

    private final ReaktorRule reaktor = new ReaktorRule()
            .directory("target/nukleus-itests")
            .commandBufferCapacity(1024)
            .responseBufferCapacity(1024)
            .counterValuesBufferCapacity(1024)
            .nukleus("http-cache"::equals)
            .clean();

    @Rule
    public final TestRule chain = outerRule(reaktor).around(k3po).around(timeout);

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/proxy.get.request/accept/client",
        "${streams}/proxy.get.request/connect/server",
        })
    public void shouldProxyGetRequest() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/proxy.get.request.with.body/accept/client",
        "${streams}/proxy.get.request.with.body/connect/server",
        })
    public void shouldProxyGetRequestWithBody() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/proxy.post.request/accept/client",
        "${streams}/proxy.post.request/connect/server",
    })
    public void shouldProxyPostRequest() throws Exception
    {
        k3po.finish();
    }


    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/proxy.request.and.304/accept/client",
        "${streams}/proxy.request.and.304/connect/server",
    })
    public void shouldProxyRequestWith304() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/cache.max-age/accept/client",
        "${streams}/cache.max-age/connect/server",
    })
    public void shouldCacheMaxAge() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
            "${route}/proxy/controller",
            "${streams}/request.cache.max-age=0/accept/client",
            "${streams}/request.cache.max-age=0/connect/server",
    })
    public void shouldRequestCacheMaxAgeZero() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
            "${route}/proxy/controller",
            "${streams}/request.cache.max-age=0.and.304/accept/client",
            "${streams}/request.cache.max-age=0.and.304/connect/server",
    })
    public void shouldRequestCacheMaxAgeZeroAnd304() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
            "${route}/proxy/controller",
            "${streams}/cache.get.request.with.no-store/accept/client",
            "${streams}/cache.get.request.with.no-store/connect/server",
    })
    public void shouldCacheGetRequestWithNoStore() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
            "${route}/proxy/controller",
            "${streams}/cache.get.request.with.no-store.and.response.marked.cacheable/accept/client",
            "${streams}/cache.get.request.with.no-store.and.response.marked.cacheable/connect/server",
    })
    public void shouldCacheGetRequestWithNoStoreAndResponeMarkedCacheable() throws Exception
    {
        k3po.start();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/expire.max-age/accept/client",
        "${streams}/expire.max-age/connect/server",
    })
    public void shouldExpireMaxAge() throws Exception
    {
        k3po.start();
        k3po.awaitBarrier("REQUEST_CACHED");
        sleep(1000);
        k3po.notifyBarrier("CACHE_EXPIRED");
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/should.bypass.cache.on.no.cache/accept/client",
        "${streams}/should.bypass.cache.on.no.cache/connect/server",
    })
    public void shouldBypassCacheOnNoCache() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/cache.s-maxage/accept/client",
        "${streams}/cache.s-maxage/connect/server",
    })
    public void shouldCacheSMaxage() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/expire.s-maxage/accept/client",
        "${streams}/expire.s-maxage/connect/server",
    })
    public void shouldExpireSMaxage() throws Exception
    {
        k3po.start();
        k3po.awaitBarrier("REQUEST_CACHED");
        sleep(1000);
        k3po.notifyBarrier("CACHE_EXPIRED");
        k3po.finish();
    }

    // TODO expires headers
    // TODO quoted maxage header
    // TODO quoted smaxage header

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/cache.by.default.for.5.seconds/accept/client",
        "${streams}/cache.by.default.for.5.seconds/connect/server",
    })
    public void shouldCacheDefaultCacheableFor5Seconds() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/expire.cache.by.default.for.5.seconds/accept/client",
        "${streams}/expire.cache.by.default.for.5.seconds/connect/server",
    })
    public void shouldExpireCacheDefaultCacheableFor5Seconds() throws Exception
    {
        k3po.start();
        k3po.awaitBarrier("REQUEST_CACHED");
        sleep(5000);
        k3po.notifyBarrier("CACHE_EXPIRED");
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/cache.by.default.for.10.percent.of.last-modified/accept/client",
        "${streams}/cache.by.default.for.10.percent.of.last-modified/connect/server",
    })
    public void shouldCacheDefaultFor10PercentOfLastModified() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/expire.cache.by.default.for.10.percent.of.last-modified/accept/client",
        "${streams}/expire.cache.by.default.for.10.percent.of.last-modified/connect/server",
    })
    public void shouldExpireCacheDefaultFor10PercentOfLastModified() throws Exception
    {
        k3po.start();
        k3po.awaitBarrier("REQUEST_CACHED");
        sleep(5000);
        k3po.notifyBarrier("CACHE_EXPIRED");
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/private.cache/accept/client",
        "${streams}/private.cache/connect/server",
    })
    public void shouldNotUsePrivateCache() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/implied.private.cache/accept/client",
        "${streams}/implied.private.cache/connect/server",
    })
    public void shouldNotUseImpliedPrivateCache() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/explicitly.public.cache/accept/client",
        "${streams}/explicitly.public.cache/connect/server",
    })
    public void shouldUseExplicitlyPublicCache() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/not.use.cache.that.varys/accept/client",
        "${streams}/not.use.cache.that.varys/connect/server",
    })
    public void shouldNotUseCacheForRequestThatVarys() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/cache.that.varys.but.matches/accept/client",
        "${streams}/cache.that.varys.but.matches/connect/server",
    })
    public void shouldUseCacheForRequestThatMatchesVarys() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "${route}/proxy/controller",
        "${streams}/proxy.response.too.large.to.cache/accept/client",
        "${streams}/proxy.response.too.large.to.cache/connect/server",
    })
    public void shouldProxyResponseTooLargeToCache() throws Exception
    {
        k3po.finish();
    }
}
